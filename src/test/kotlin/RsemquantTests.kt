import org.junit.jupiter.api.*
import step.*
import testutil.*
import java.nio.file.*
import org.assertj.core.api.Assertions.*

class RSEMQuantTests {

    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `test RSEM unstranded, single end`() {
       
        cmdRunner.runRSEMQuant(
            RSEMParameters(
                bam = getResourcePath("ENCFF648GBR.chrM.subsampled.bam"),
                index = getResourcePath("test.index.tar.gz"),
                outputDirectory = testDir,
                outputPrefix = "output",
                pairedEnd = false
            )
        )

        assertThat(testDir.resolve("output.genes.results")).exists()
        assertThat(pearsonr(
            readQuantifications(testDir.resolve("output.genes.results")),
            readQuantifications(getResourcePath("genes.expected.results"))
        )).isGreaterThan(0.9F)

        assertThat(testDir.resolve("output.isoforms.results")).exists()
        assertThat(pearsonr(
            readQuantifications(testDir.resolve("output.isoforms.results")),
            readQuantifications(getResourcePath("isoforms.expected.results"))
        )).isGreaterThan(0.9F)
        
    }

}
