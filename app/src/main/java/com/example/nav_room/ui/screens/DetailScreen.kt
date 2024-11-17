package com.example.nav_room.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.nav_room.data.Note
import com.example.nav_room.ui.NoteViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    noteId: Int?
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val photoFile = remember { File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg") }

    // Memuat data note berdasarkan noteId
    LaunchedEffect(noteId) {
        noteId?.let {
            val note = viewModel.getNoteById(it)
            note?.let {
                title = it.title
                content = it.content
                photoUri = it.photoUri
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Menampilkan foto yang sudah dipilih atau diambil
        if (photoUri != null) {
            AsyncImage(
                model = Uri.parse(photoUri),
                contentDescription = "Note Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button untuk mengambil foto menggunakan kamera
        Button(
            onClick = {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().apply {
                        setSurfaceProvider { /* No-op: Camera preview not used */ }
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, // Menggunakan LocalLifecycleOwner
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                }, ContextCompat.getMainExecutor(context))

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CameraX", "Photo capture failed: ${exception.message}")
                        }

                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            photoUri = Uri.fromFile(photoFile).toString()
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button untuk memilih gambar dari galeri
        Button(
            onClick = {
                // Membuka galeri untuk memilih gambar
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                (context as? Activity)?.startActivityForResult(intent, PICK_IMAGE_REQUEST)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Photo from Gallery")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol untuk menyimpan atau memperbarui catatan
        Button(
            onClick = {
                if (noteId == null) {
                    viewModel.addNote(Note(0, title, content, photoUri))
                } else {
                    viewModel.updateNote(Note(noteId, title, content, photoUri))
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (noteId == null) "Save Note" else "Update Note")
        }
    }
}

// Menangani hasil gambar yang dipilih
const val PICK_IMAGE_REQUEST = 1001

fun handleImageResult(resultCode: Int, data: Intent?, context: Context, onImagePicked: (Uri) -> Unit) {
    if (resultCode == Activity.RESULT_OK) {
        data?.data?.let {
            onImagePicked(it)
        }
    }
}