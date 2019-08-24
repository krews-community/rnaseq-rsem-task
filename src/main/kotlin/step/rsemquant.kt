package step
import mu.KotlinLogging
import util.*
import java.nio.file.*
import util.CmdRunner
import java.io.BufferedReader
import java.io.FileReader
import com.squareup.moshi.Moshi
import java.io.File
import java.time.LocalDate
import java.util.*

private val log = KotlinLogging.logger {}
var moshi = Moshi.Builder().build()
data class qcr(val no_of_genes_detected:Int)

fun CmdRunner.rsemquant(rsemIndex:Path,annoBamFile:Path, pairedEnd:Boolean,readStrand:String,rndSeed:Int,ncpus:Int,ramGB:Int,outDir:Path,outputPrefix:String)  {
    log.info { "Make output Directory" }
    Files.createDirectories(outDir)
    //untar index file
    val endness = if(pairedEnd) "--paired-end" else ""
    val rStrand = if(readStrand=="Unstranded") 0.5 else if(readStrand=="forward")  1 else 0
   this.run("tar xvf $rsemIndex -C $outDir")
    val RSEM_COMMAND = "rsem-calculate-expression --bam \\\n" +
    "--estimate-rspd \\\n"+
    "--calc-ci \\\n"+
    "--seed ${rndSeed} \\\n" +
    "-p ${ncpus} \\\n" +
    "--no-bam-output \\\n" +
    "--ci-memory ${ramGB}000 \\\n" +
    "--forward-prob ${rStrand} \\\n" +
    "${endness} \\\n" +
    "${annoBamFile} \\\n" +
    "${outDir.resolve("out").resolve("rsem")} \\\n" +
    "${outDir.resolve("${outputPrefix}_rsem")} \\\n"
        //    "--outFileNamePrefix ${outDir}/testrsemquant"

    this.run(RSEM_COMMAND)

  //  this.run("mv ${outDir.resolve("out").resolve("*.genes.results")} ${outDir}")
  //  this.run("mv ${outDir.resolve("out").resolve("*.isoforms.results")} ${outDir}")


/*    val  gene_quant_fn = "${outputPrefix}_rsem.genes.results"

    val gCount = calculate_number_of_genes_detected(gene_quant_fn)
    var gene_qc = qcr(gCount)
    val jsonAdapter = moshi.adapter(qcr::class.java)

    val json = jsonAdapter.toJson(gene_qc)

    val qcFile = outDir.resolve("${outputPrefix}_number_of_genes_detected.json")
    File(qcFile.toString()).writeText(json)*/



}
