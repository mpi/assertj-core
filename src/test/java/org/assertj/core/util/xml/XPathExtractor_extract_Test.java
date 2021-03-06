/*
 * Created on Feb 8, 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2013 the original author or authors.
 */
package org.assertj.core.util.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tests for <code>{@link XPathExtractor#extract(String)}</code>.
 *
 * @author Michał Piotrkowski
 */
public class XPathExtractor_extract_Test {

  @Test
  public void should_extract_element() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor("<name>John Doe</name>");
    
    NodeList extracted = extractor.extract("/name");
    
    assertThat(extracted.getLength()).isEqualTo(1);
    assertThat(extracted.item(0).getNodeType()).isEqualTo(Node.ELEMENT_NODE);
    assertThat(extracted.item(0).getNodeName()).isEqualTo("name");
    assertThat(extracted.item(0).getTextContent()).isEqualTo("John Doe");
  }
  
  @Test
  public void should_extract_element_text() throws Exception {

    XPathExtractor extractor = new XPathExtractor("<name>John Doe</name>");
    
    NodeList extracted = extractor.extract("/name/text()");
    
    assertThat(extracted.getLength()).isEqualTo(1);
    assertThat(extracted.item(0).getNodeType()).isEqualTo(Node.TEXT_NODE);
    assertThat(extracted.item(0).getNodeValue()).isEqualTo("John Doe");
    assertThat(extracted.item(0).getNodeName()).isEqualTo("#text");
  }
  
  @Test
  public void should_extract_elements() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor("<person><first-name>John</first-name><last-name>Doe</last-name></person>");
    
    NodeList extracted = extractor.extract("/person/*");
    
    assertThat(extracted.getLength()).isEqualTo(2);
    assertThat(extracted.item(0).getNodeName()).isEqualTo("first-name");
    assertThat(extracted.item(1).getNodeName()).isEqualTo("last-name");
  }

  @Test
  public void should_extract_nested_elements() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor("<person><first-name>John</first-name><last-name>Doe</last-name></person>");
    
    NodeList extracted = extractor.extract("//first-name");
    
    assertThat(extracted.getLength()).isEqualTo(1);
    assertThat(extracted.item(0).getNodeName()).isEqualTo("first-name");
  }

  @Test
  public void should_extract_attributes() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor("<person first-name='John' last-name='Doe'/>");
    
    NodeList extracted = extractor.extract("/person/@*");
    
    assertThat(extracted.getLength()).isEqualTo(2);
    assertThat(extracted.item(0).getNodeType()).isEqualTo(Node.ATTRIBUTE_NODE);
    assertThat(extracted.item(0).getNodeName()).isEqualTo("first-name");
    assertThat(extracted.item(1).getNodeName()).isEqualTo("last-name");
  }

  @Test
  public void should_extract_attribute_equal() throws Exception {
    
    XPathExtractor extractor1 = new XPathExtractor("<person first-name='John' last-name='Doe'/>");
    XPathExtractor extractor2 = new XPathExtractor("<person last-name='Doe' first-name='John'/>");
    
    Node node1 = extractor1.extract("/person/@*").item(0);
    Node node2 = extractor2.extract("/person/@*").item(0);
    
    assertThat(node1.isEqualNode(node2)).isTrue();
  }

  @Ignore
  @Test
  public void should_extract_attribute_text() throws Exception {

    XPathExtractor extractor = new XPathExtractor("<name value='John Doe'/>");
    
    NodeList extracted = extractor.extract("string(/name/@value)");
    
    assertThat(extracted.getLength()).isEqualTo(1);
    assertThat(extracted.item(0).getNodeType()).isEqualTo(Node.TEXT_NODE);
    assertThat(extracted.item(0).getNodeValue()).isEqualTo("John Doe");
    assertThat(extracted.item(0).getNodeName()).isEqualTo("#text");
  }

  
  @Test
  public void should_extract_attribute_and_element() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor("<person vip='true'><first-name>John</first-name><last-name>Doe</last-name></person>");
    
    NodeList extracted = extractor.extract("/person/@* | /person/*");
    
    assertThat(extracted.getLength()).isEqualTo(3);
    assertThat(extracted.item(0).getNodeType()).isEqualTo(Node.ATTRIBUTE_NODE);
    assertThat(extracted.item(1).getNodeType()).isEqualTo(Node.ELEMENT_NODE);
    assertThat(extracted.item(2).getNodeType()).isEqualTo(Node.ELEMENT_NODE);
  }

  @Test
  public void should_chain_extract_element_from_element() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor(
        "<document>" +
            "<element attribute='abc'/>" +
            "<otherElement attribute='def'/>" +
        "</document>");
    
    NodeList firstLevel = extractor.extract("/document/element");
    NodeList secondLevel = new XPathExtractor(firstLevel).extract("/element");
    
    assertThat(secondLevel.getLength()).isEqualTo(1);
    
  }
  
  @Test
  public void should_chain_extract_attribute_from_element() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor(
        "<document>" +
            "<element attribute='abc'/>" +
            "<otherElement attribute='def'/>" +
        "</document>");
    
    NodeList firstLevel = extractor.extract("/document/element");
    NodeList secondLevel = new XPathExtractor(firstLevel).extract("@attribute");
    
    assertThat(secondLevel.getLength()).isEqualTo(1);
  }
  
  @Test
  public void should_chain_extract_text_from_element() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor(
        "<document>" +
            "<element attribute='abc'>text</element>" +
            "<otherElement attribute='def'/>" +
        "</document>");
    
    NodeList firstLevel = extractor.extract("/document/element");
    NodeList secondLevel = new XPathExtractor(firstLevel).extract("text()");
    
    assertThat(secondLevel.getLength()).isEqualTo(1);
  }
  
  @Test
  public void should_chain_extract_comment_from_element() throws Exception {
    
    XPathExtractor extractor = new XPathExtractor(
        "<document>" +
            "<element attribute='abc'><!--comment--></element>" +
            "<otherElement attribute='def'/>" +
        "</document>");
    
    NodeList firstLevel = extractor.extract("/document/element");
    NodeList secondLevel = new XPathExtractor(firstLevel).extract("comment()");
    
    assertThat(secondLevel.getLength()).isEqualTo(1);
  }
  
}
