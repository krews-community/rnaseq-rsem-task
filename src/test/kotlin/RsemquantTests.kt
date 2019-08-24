import org.junit.jupiter.api.*
import step.*
import testutil.*
import testutil.cmdRunner
import testutil.setupTest
import java.nio.file.*
import org.assertj.core.api.Assertions.*

class RsemquantTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

     @Test fun `run rsem quant file `() {

     //    cmdRunner.rsem(F1,null, CTL_INDEX, false,"star","libraryID",4,15, testOutputDir,"testaligner")
         //assertThat(testOutputDir.resolve("mergedta.pooled.tagAlign.gz")).exists()

    }

}