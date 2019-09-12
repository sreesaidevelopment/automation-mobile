package olx.kz;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="features",
				glue="stepDefinations",
				plugin = {"com.cucumber.listener.ExtentCucumberFormatter:output/olxReport.html"},
				dryRun=false)
public class runTest {

}