package step

import util.*
import java.nio.file.*
import util.CmdRunner
import java.io.File
import java.util.*
import kotlin.io.createTempDir

data class RSEMParameters (
    val index: Path,
    val bam: Path,
    val outputDirectory: Path,
    val strand: String = "unstranded",
    val seed: Int? = null,
    val cores: Int = 1,
    val ram: Int = 16,
    val outputPrefix: String = "output",
    val pairedEnd: Boolean
)

val FORWARD_PROB: Map<String, Float> = mapOf(
    "unstranded" to 0.5F,
    "forward" to 1.0F,
    "reverse" to 0.0F
)

fun CmdRunner.runRSEMQuant(parameters: RSEMParameters)  {

    // create output directory, unpack index
    val indexDir = parameters.outputDirectory.resolve("index")
    Files.createDirectories(indexDir)
    this.run("tar xvf ${parameters.index} -C $indexDir")
    
    // run RSEM
    this.run("""
        rsem-calculate-expression \
            --bam \
            --estimate-rspd \
            --calc-ci \
            ${if (parameters.seed !== null) "--seed ${parameters.seed}" else "" } \
            -p ${parameters.cores} \
            --no-bam-output \
            --ci-memory ${parameters.ram}000 \
            --forward-prob ${FORWARD_PROB[parameters.strand]} \
            ${ if (parameters.pairedEnd) "--paired-end" else "" } \
            ${parameters.bam} \
            ${indexDir.resolve(parameters.index.getFileName().toString().split(".tar.gz")[0])} \
            ${parameters.outputDirectory.resolve("${parameters.outputPrefix}")}
    """)

    // delete unpacked index
    indexDir.toFile().deleteRecursively()

}
