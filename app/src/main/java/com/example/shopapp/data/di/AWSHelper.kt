package com.example.shopapp.data.di

import android.content.Context
import android.net.Uri
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.AWSCredentialsProvider
import com.example.shopapp.common.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.io.File

class AWSHelper(private val context: Context) {
    private lateinit var s3Client: AmazonS3Client
    private lateinit var transferUtility: TransferUtility
    private lateinit var awsConfiguration: AWSConfiguration

    suspend fun initAWS() = suspendCancellableCoroutine { continuation ->
        try {
            // Load configuration from assets
            awsConfiguration = AWSConfiguration(context)
            AWSMobileClient.getInstance().initialize(context, awsConfiguration, object : Callback<UserStateDetails> {
                override fun onResult(result: UserStateDetails) {
                    try {
                        s3Client = AmazonS3Client(AWSMobileClient.getInstance() as AWSCredentialsProvider)
                        transferUtility = TransferUtility.builder()
                            .context(context)
                            .s3Client(s3Client)
                            .awsConfiguration(awsConfiguration)
                            .build()
                        continuation.resume(Unit)
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    }
                }

                override fun onError(e: Exception) {
                    continuation.resumeWithException(e)
                }
            })
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    fun uploadFile(filePath: String, objectKey: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val bucketName = awsConfiguration.optJsonObject("S3TransferUtility")
                ?.getString("Bucket") ?: throw Exception("Bucket name not found")

            val uploadObserver = transferUtility.upload(bucketName, objectKey, File(filePath))

            uploadObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state == TransferState.COMPLETED) {
                        val s3Url = "https://$bucketName.s3.amazonaws.com/$objectKey"
                        trySend(ResultState.Success(s3Url))
                        channel.close()
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    val percentDone = ((bytesCurrent.toDouble() / bytesTotal) * 100).toInt()
                    // You can use trySend here if you want to emit progress
                    // trySend(ResultState.Loading(percentDone))
                }

                override fun onError(id: Int, ex: Exception) {
                    trySend(ResultState.Error(ex.message ?: "Upload failed"))
                    channel.close(ex)
                }
            })

            awaitClose {
                uploadObserver.cleanTransferListener()
            }
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Upload failed"))
            channel.close()
        }
    }

    fun uploadFileFromUri(context: Context, uri: Uri, objectKey: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val bucketName = awsConfiguration.optJsonObject("S3TransferUtility")
                ?.getString("Bucket") ?: throw Exception("Bucket name not found")

            // Copy content from Uri to temp file
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw Exception("Unable to open input stream from URI")
            val tempFile = File.createTempFile("upload", null, context.cacheDir)
            tempFile.outputStream().use { fileOut ->
                inputStream.copyTo(fileOut)
            }
            inputStream.close()

            val uploadObserver = transferUtility.upload(bucketName, objectKey, tempFile)

            uploadObserver.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState) {
                    if (state == TransferState.COMPLETED) {
                        val s3Url = "https://$bucketName.s3.amazonaws.com/$objectKey"
                        trySend(ResultState.Success(s3Url))
                        channel.close()
                        tempFile.delete()
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    // Optionally emit progress
                }

                override fun onError(id: Int, ex: Exception) {
                    trySend(ResultState.Error(ex.message ?: "Upload failed"))
                    channel.close(ex)
                    tempFile.delete()
                }
            })

            awaitClose {
                uploadObserver.cleanTransferListener()
                tempFile.delete()
            }
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Upload failed"))
            channel.close()
        }
    }

    fun deleteFile(objectKey: String): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        try {
            val bucketName = awsConfiguration.optJsonObject("S3TransferUtility")
                ?.getString("Bucket") ?: throw Exception("Bucket name not found")

            s3Client.deleteObject(bucketName, objectKey)
            emit(ResultState.Success(true))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Delete failed"))
        }
    }

    fun getFileUrl(objectKey: String): String {
        val bucketName = awsConfiguration.optJsonObject("S3TransferUtility")
            ?.getString("Bucket") ?: throw Exception("Bucket name not found")
        return "https://$bucketName.s3.amazonaws.com/$objectKey"
    }
}
