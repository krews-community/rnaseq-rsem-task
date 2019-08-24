import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import step.*
import util.*
import java.nio.file.*
import util.CmdRunner


fun main(args: Array<String>) = Cli().main(args)

class Cli : CliktCommand() {
    private val rsemIndex: Path by option("-rsemindex", help = "path to rsem index file")
            .path().required()
    private val annoBamFile:Path by option("-annobam", help = "path to annotation bam file")
            .path().required()

    private val pairedEnd: Boolean by option("-pairedEnd", help = "Paired-end BAM.").flag()
    private val readStrand:String by option("-readstrand",help = "read strand").choice("forward","reverse","Unstranded").required()
    private val rndSeed: Int by option("-randomseed", help = "Number of cpus available.").int().default(12345)

    private val outputPrefix: String by option("-outputPrefix", help = "output file name prefix; defaults to 'output'").default("output")
    private val outDir by option("-outputDir", help = "path to output Directory")
        .path().required()
    private val ncpus: Int by option("-ncpus", help = "Number of cpus available.").int().default(4)
    private val ramGB: Int by option("-ramGB", help = "Amount of RAM available in GB").int().default(8)

    override fun run() {
        val cmdRunner = DefaultCmdRunner()
        cmdRunner.runTask(rsemIndex,annoBamFile, pairedEnd,readStrand,rndSeed,ncpus,ramGB,outDir,outputPrefix)
    }
}

/**
 * Runs pre-processing and bwa for raw input files
 *
 * @param taFiles pooledTa Input
 * @param outDir Output Path
 */
fun CmdRunner.runTask(rsemIndex:Path,annoBamFile:Path, pairedEnd:Boolean,readStrand:String,rndSeed:Int,ncpus:Int,ramGB:Int,outDir:Path,outputPrefix:String) {

    rsemquant(rsemIndex,annoBamFile, pairedEnd,readStrand,rndSeed,ncpus,ramGB,outDir,outputPrefix)
}