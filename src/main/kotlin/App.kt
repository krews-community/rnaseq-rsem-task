import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import step.*
import util.*
import java.nio.file.*
import util.CmdRunner

fun main(args: Array<String>) = Cli().main(args)

class Cli : CliktCommand() {

    private val index: Path by option("--index", help = "path to RSEM index tarball")
        .path().required()
    private val bam: Path by option("--bam", help = "path to BAM file containing transcriptome alignments")
        .path().required()
    private val pairedEnd: Boolean by option("--paired-end", help = "specifies that the input BAM is paired-end").flag()
    private val strand: String by option("--strand",help = "specifies that reads belong to the given genomic strand")
        .choice("forward", "reverse", "unstranded").default("unstranded")
    private val seed: Int? by option("--seed", help = "if provided, uses the given seed for random number generation")
        .int()

    private val outputPrefix: String by option("--output-prefix", help = "output file name prefix; defaults to 'output'").default("output")
    private val outputDirectory by option("-output-directory", help = "path to output directory")
        .path().required()
    private val cores: Int by option("--cores", help = "number of cores available to the task").int().default(1)
    private val ram: Int by option("--ram-gb", help = "amount of RAM available to the task, in GB").int().default(16)

    override fun run() {
        DefaultCmdRunner().runRSEMQuant(
            RSEMParameters(
                index = index,
                bam = bam,
                pairedEnd = pairedEnd,
                strand = strand,
                seed = seed,
                outputPrefix = outputPrefix,
                outputDirectory = outputDirectory,
                cores = cores,
                ram = ram
            )
        )
    }

}
