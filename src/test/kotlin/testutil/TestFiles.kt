package testutil
import java.nio.file.*
import kotlin.io.createTempDir

fun getResourcePath(relativePath: String): Path {
    val url = TestCmdRunner::class.java.classLoader.getResource(relativePath)
    return Paths.get(url.toURI())
}

// Resource Directories
val testInputResourcesDir = getResourcePath("ENCFF648GBR.chrM.subsampled.bam").getParent()

// Test Working Directories
val testDir = createTempDir().toPath()
