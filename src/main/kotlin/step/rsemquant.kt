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
    val pairedEnd: Boolean,
    val indexTarPrefix: String? = null,
    val indexNamePrefix: String? = null
)

val FORWARD_PROB: Map<String, Float> = mapOf(
    "unstranded" to 0.5F,
    "forward" to 1.0F,
    "reverse" to 0.0F
)

fun CmdRunner.runRSEMQuant(parameters: RSEMParameters)  {

    // create output directory, unpack index
    this.run("tar xvf ${parameters.index} -C ${parameters.outputDirectory}")
    if (parameters.indexTarPrefix !== null) this.run("mv ${parameters.outputDirectory}/${parameters.indexTarPrefix}/* ${parameters.outputDirectory}")
    val indexNamePrefix = if (parameters.indexNamePrefix !== null) parameters.indexNamePrefix else parameters.index.getFileName().toString().split(".tar.gz")[0]
    
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
            ${parameters.outputDirectory.resolve(indexNamePrefix)} \
            ${parameters.outputDirectory.resolve("${parameters.outputPrefix}")}
    """)

    // delete unnecessary stat directory
    parameters.outputDirectory.resolve("${parameters.outputPrefix}.stat").toFile().deleteRecursively()

}
