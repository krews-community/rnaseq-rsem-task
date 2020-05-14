package testutil

import java.nio.file.*
import util.CmdRunner
import util.*
import java.security.MessageDigest

val cmdRunner = TestCmdRunner()

class TestCmdRunner : CmdRunner {
    override fun run(cmd: String) = exec("docker", "exec", "rnaseq-quant-base", "sh", "-c", cmd)
    override fun runCommand(cmd: String): String? = getCommandOutput("docker", "exec", "rnaseq-quant-base", "sh", "-c", cmd)
}

fun copyDirectory(fromDir: Path, toDir: Path) {
    Files.walk(fromDir).forEach { fromFile ->
        if (Files.isRegularFile(fromFile)) {
            val toFile = toDir.resolve(fromDir.relativize(fromFile))
            Files.createDirectories(toFile.parent)
            Files.copy(fromFile, toFile)
        }
    }
}

fun setupTest() {
    exec(
        "docker", "run", "--name", "rnaseq-quant-base", "--rm", "-i",
        "-t", "-d", "-v", "${testInputResourcesDir}:${testInputResourcesDir}",
        "-v", "${testDir}:${testDir}", "genomealmanac/rnaseq-quant-base"
    )
}

fun cleanupTest() {
    testDir.toFile().deleteRecursively()
}

fun pearsonr(a: Map<String, Float>, b:Map<String, Float>): Float {
    val keys = a.keys.toList()
    return pearsonr(keys.map { a[it]!! }, keys.map { b[it]!! })
}

fun pearsonr(a: List<Float>, b: List<Float>): Float {
    
    var sumX: Float = 0.0F
    var sumY: Float = 0.0F
    var sumXY: Float = 0.0F
    var squareSumX: Float = 0.0F
    var squareSumY: Float = 0.0F

    a.forEachIndexed { i, it ->
        sumX += it
        sumY += b[i]
        sumXY += it * b[i]
        squareSumX += it * it
        squareSumY += b[i] * b[i]
    }

    return ((a.size * sumXY - sumX * sumY) / Math.sqrt(
        ((a.size * squareSumX - sumX * sumX) * (a.size * squareSumY - sumY * sumY)).toDouble()
    )).toFloat()

}

fun readQuantifications(file: Path): Map<String, Float> {
    val m: MutableMap<String, Float> = mutableMapOf()
    file.toFile().forEachLine {
        val s = it.split('\t')
        if (s.size < 6 || s[5] == "TPM") return@forEachLine
        m[s[1]] = s[5].toFloat()
    }
    return m
}
