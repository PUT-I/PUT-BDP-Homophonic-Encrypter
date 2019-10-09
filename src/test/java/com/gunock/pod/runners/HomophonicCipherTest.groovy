package com.gunock.pod.runners

import io.cucumber.testng.AbstractTestNGCucumberTests
import io.cucumber.testng.CucumberOptions

@CucumberOptions(
        features = "src/test/java/com/gunock/pod/features/HomophonicCipher.feature",
        glue = "com.gunock.pod.features.steps"
)
class HomophonicCipherTest extends AbstractTestNGCucumberTests {

}
