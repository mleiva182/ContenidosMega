package com.mleiva.contenidosmega

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mleiva.contenidosmega.model.Contenido
import com.mleiva.contenidosmega.ui.contenidos.ContenidosScreen
import com.mleiva.contenidosmega.ui.videoplayer.VideoPlayerScreen
import com.mleiva.contenidosmega.ui.contenidos.ContenidosViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = ContenidosViewModel()

            NavHost(
                navController = navController,
                startDestination = "lista"
            ) {
                composable("lista") {
                    ContenidosScreen(viewModel) { contenido ->
                        navController.navigate(
                            "detalle/${Uri.encode(contenido.videoUrl)}/${Uri.encode(contenido.titulo)}/${Uri.encode(contenido.imagenUrl)}"
                        )
                    }
                }

                composable(
                    route = "detalle/{videoUrl}/{titulo}/{imagenUrl}",
                    arguments = listOf(
                        navArgument("videoUrl") { type = NavType.StringType },
                        navArgument("titulo") { type = NavType.StringType },
                        navArgument("imagenUrl") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                    val titulo = backStackEntry.arguments?.getString("titulo") ?: ""
                    val imagenUrl = backStackEntry.arguments?.getString("imagenUrl") ?: ""

                    VideoPlayerScreen(
                        contenido = Contenido(titulo = titulo, videoUrl = videoUrl, imagenUrl = imagenUrl),
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

        }
    }
}
