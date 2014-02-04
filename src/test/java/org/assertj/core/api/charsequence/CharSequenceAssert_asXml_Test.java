package org.assertj.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class CharSequenceAssert_asXml_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void should_fail_if_invalid_xml_was_passed() throws Exception {

    // expect:
    thrown.expectAssertionError("Expecting xml document but was:<\"invalidXml\">");
    
    // when:
    assertThat("invalidXml").asXml();
  }
  
  @Test
  public void should_pass_if_valid_xml_was_passed() throws Exception {
    
    // when:
    assertThat("<validXml/>").asXml();
  }
  
  @Test
  public void should_fail_if_null_xml_was_passed() throws Exception {
    
    // given:
    String nullXml = null;
    // expect:
    thrown.expectAssertionError("Expecting actual not to be null");
    // when:
    assertThat(nullXml).asXml();
  }
  
}
