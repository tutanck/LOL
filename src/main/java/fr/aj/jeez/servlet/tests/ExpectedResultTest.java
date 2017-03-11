package fr.aj.jeez.servlet.tests;

import java.util.Set;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Anagbla Joan */
public class ExpectedResultTest {
	
	//TODO passer a une map pour type les key du result et anisi verifier le typage static 
	@Test
	public void ResultShouldContainExpected(
			JSONObject result,
			Set<String> epnOut
			){
		for(String expected : epnOut){
			Assert.assertTrue(
					"{result} should at least contain all keys in {epnOut}",
					resultWellFormed(result,expected)
					);
		}
	}

	
	//TODO verifier le typage des key du resultat 
	private boolean resultWellFormed(
			JSONObject result,
			String expected
			){
		return result.has(expected);
	}
	
}
