/**
 * File: AbstractParserTest.java
 * Original Author: Dan Brown <dan@likethecolor.com>
 * Copyright 2012 Dan Brown <dan@likethecolor.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likethecolor.alchemy.api.parser.json;

import com.likethecolor.alchemy.api.entity.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AbstractParserTest {
  private static final String LANGUAGE = "english";
  private static final String STATUS_STRING = "OK";
  private static final Response.STATUS STATUS = Response.STATUS.OK;
  private static final String STATUS_INFO = "Everything is good";
  private static final String TEXT = "this is some text";
  private static final String USAGE = "By accessing AlchemyAPI or using information generated by AlchemyAPI, you are agreeing to be bound by the AlchemyAPI Terms of Use: http://www.alchemyapi.com/company/terms.html";
  private static final String URL = "http://www.bartleby.com/124/pres32.html";
  private static final String BOOLEAN_KEY = "boolean";
  private static final Boolean BOOLEAN = true;
  private static final String BOOLEAN_NON_STANDARD = "yes";
  private static final String DOUBLE_KEY = "double";
  private static final Double DOUBLE = 123.1231312D;
  private static final String INTEGER_KEY = "integer";
  private static final Integer INTEGER = 12312;
  private static final String LONG_KEY = "long";
  private static final Long LONG = 12312313121L;
  private static final String STRING_KEY = "String";
  private static final String STRING = "This is a string";
  private static final String OBJECT_KEY = "object";
  private static final String OBJECT_TEXT_KEY = "otext";
  private static final String OBJECT_TEXT_VALUE = "value";
  private static final String MAP_KEY = "map";
  private static final String STRING_ARRAY_KEY = "stringarray";
  private static final String NON_STRING_ARRAY_KEY = "nonstringarray";
  private static final String ARRAY_TEXT0_KEY = "text0";
  private static final String ARRAY_TEXT1_KEY = "text1";
  private static final String ARRAY_TEXT2_KEY = "text2";
  private static final String TEXT0 = "civil war";
  private static final String TEXT1 = "impending civil war";
  private static final String TEXT2 = "Justice Salmon Chase";

  @Test
  public void testParse() {
    final AbstractParser parser = new MockAbstractParser();

    final Response actualResponse = parser.parse(getJsonString());

    assertEquals(LANGUAGE, actualResponse.getLanguage());
    assertEquals(STATUS, actualResponse.getStatus());
    assertEquals(STATUS_INFO, actualResponse.getStatusInfo());
    assertEquals(TEXT, actualResponse.getText());
    assertEquals(URL, actualResponse.getURL());
    assertEquals(USAGE, actualResponse.getUsage());
  }

  @Test
  public void testParse_NoLanguage() {
    final AbstractParser parser = new MockAbstractParser();

    final Response actualResponse = parser.parse(getJsonString_NoLanguage());

    assertNull(actualResponse.getLanguage());
    assertEquals(STATUS, actualResponse.getStatus());
    assertEquals(STATUS_INFO, actualResponse.getStatusInfo());
    assertEquals(TEXT, actualResponse.getText());
    assertEquals(URL, actualResponse.getURL());
    assertEquals(USAGE, actualResponse.getUsage());
  }

  @Test
  public void testParse_NoStatus() {
    final AbstractParser parser = new MockAbstractParser();

    final Response actualResponse = parser.parse(getJsonString_NoStatus());

    assertEquals(LANGUAGE, actualResponse.getLanguage());
    assertEquals(Response.STATUS.UNSET, actualResponse.getStatus());
    assertNull(actualResponse.getStatusInfo());
    assertEquals(TEXT, actualResponse.getText());
    assertEquals(URL, actualResponse.getURL());
    assertEquals(USAGE, actualResponse.getUsage());
  }

  @Test
  public void testParse_NoText() {
    final AbstractParser parser = new MockAbstractParser();

    final Response actualResponse = parser.parse(getJsonString_NoText());

    assertEquals(LANGUAGE, actualResponse.getLanguage());
    assertEquals(STATUS, actualResponse.getStatus());
    assertEquals(STATUS_INFO, actualResponse.getStatusInfo());
    assertNull(actualResponse.getText());
    assertEquals(URL, actualResponse.getURL());
    assertEquals(USAGE, actualResponse.getUsage());
  }

  @Test
  public void testParse_NoUrl() {
    final AbstractParser parser = new MockAbstractParser();

    final Response actualResponse = parser.parse(getJsonString_NoUrl());

    assertEquals(LANGUAGE, actualResponse.getLanguage());
    assertEquals(STATUS, actualResponse.getStatus());
    assertEquals(STATUS_INFO, actualResponse.getStatusInfo());
    assertEquals(TEXT, actualResponse.getText());
    assertNull(actualResponse.getURL());
    assertEquals(USAGE, actualResponse.getUsage());
  }

  @Test
  public void testParse_NoUsage() {
    final AbstractParser parser = new MockAbstractParser();

    final Response actualResponse = parser.parse(getJsonString_NoUsage());

    assertEquals(LANGUAGE, actualResponse.getLanguage());
    assertEquals(STATUS, actualResponse.getStatus());
    assertEquals(STATUS_INFO, actualResponse.getStatusInfo());
    assertEquals(TEXT, actualResponse.getText());
    assertEquals(URL, actualResponse.getURL());
    assertNull(actualResponse.getUsage());
  }

  @Test
  public void testGetJSONObject() throws JSONException {
    final AbstractParser parser = new MockAbstractParser();

    JSONObject actualObject = parser.getJSONObject(null);
    assertEquals("{}", actualObject.toString());

    actualObject = parser.getJSONObject("");
    assertEquals("{}", actualObject.toString());

    actualObject = parser.getJSONObject("\t \r\n");
    assertEquals("{}", actualObject.toString());

    actualObject = parser.getJSONObject("{");
    assertEquals("{}", actualObject.toString());

    actualObject = parser.getJSONObject("{\"" + JSONConstants.RESULTS_LANGUAGE + "\":\"" + LANGUAGE + "\"}");
    assertEquals(LANGUAGE, actualObject.getString(JSONConstants.RESULTS_LANGUAGE));
  }

  @Test
  public void testGetBoolean() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Boolean actualBoolean = parser.getBoolean(BOOLEAN_KEY, jsonObject);

    assertEquals(BOOLEAN, actualBoolean);
  }

  @Test
  public void testGetBoolean_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Boolean actualBoolean = parser.getBoolean(BOOLEAN_KEY + "FOO_BAR", jsonObject);

    assertNull(actualBoolean);
  }

  @Test
  public void testGetBoolean_ValueIsNotBoolean() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Boolean actualBoolean = parser.getBoolean(MAP_KEY, jsonObject);

    assertFalse(actualBoolean);
  }

  @Test
  public void testGetBoolean_ValueIsNotStandardBoolean() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString_BooleanIsNonStandard());
    final AbstractParser parser = new MockAbstractParser();

    final Boolean actualBoolean = parser.getBoolean(BOOLEAN_KEY, jsonObject);

    assertTrue(actualBoolean);
  }

  @Test
  public void testGetDouble() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Double actualDouble = parser.getDouble(DOUBLE_KEY, jsonObject);

    assertEquals(DOUBLE, actualDouble);
  }

  @Test
  public void testGetDouble_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Double actualDouble = parser.getDouble(DOUBLE_KEY + "FOO_BAR", jsonObject);

    assertNull(actualDouble);
  }

  @Test
  public void testGetDouble_ValueIsNotDouble() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Double actualDouble = parser.getDouble(MAP_KEY, jsonObject);

    assertNull(actualDouble);
  }

  @Test
  public void testGetInteger() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Integer actualInteger = parser.getInteger(INTEGER_KEY, jsonObject);

    assertEquals(INTEGER, actualInteger);
  }

  @Test
  public void testGetInteger_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Integer actualInteger = parser.getInteger(INTEGER_KEY + "FOO_BAR", jsonObject);

    assertNull(actualInteger);
  }

  @Test
  public void testGetInteger_ValueIsNotInteger() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Integer actualInteger = parser.getInteger(MAP_KEY, jsonObject);

    assertNull(actualInteger);
  }

  @Test
  public void testGetLong() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Long actualLong = parser.getLong(LONG_KEY, jsonObject);

    assertEquals(LONG, actualLong);
  }

  @Test
  public void testGetLong_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Long actualLong = parser.getLong(LONG_KEY + "FOO_BAR", jsonObject);

    assertNull(actualLong);
  }

  @Test
  public void testGetLong_ValueIsNotLong() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final Long actualLong = parser.getLong(MAP_KEY, jsonObject);

    assertNull(actualLong);
  }

  @Test
  public void testGetString() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final String actualString = parser.getString(STRING_KEY, jsonObject);

    assertEquals(STRING, actualString);
  }

  @Test
  public void testGetString_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final String actualString = parser.getString(STRING_KEY + "FOO_BAR", jsonObject);

    assertNull(actualString);
  }

  @Test
  public void testGetString_ValueIsNotString() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final String actualString = parser.getString(LONG_KEY, jsonObject);

    assertEquals(LONG.toString(), actualString);
  }

  @Test
  public void testGetString_ArrayWithIndex() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString_WithArray());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray jsonArray = parser.getJSONArray(STRING_ARRAY_KEY, jsonObject);

    String actualString = parser.getString(0, jsonArray);

    assertEquals(TEXT0, actualString);

    actualString = parser.getString(1, jsonArray);

    assertEquals(TEXT1, actualString);

    actualString = parser.getString(2, jsonArray);

    assertEquals(TEXT2, actualString);
  }

  @Test
  public void testGetString_ArrayWithIndex_IndexTooLarge() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString_WithArray());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray jsonArray = parser.getJSONArray(STRING_ARRAY_KEY, jsonObject);

    String actualString = parser.getString(jsonArray.length(), jsonArray);

    assertNull(actualString);
  }

  @Test
  public void testGetString_ArrayWithIndex_IndexTooSmall() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString_WithArray());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray jsonArray = parser.getJSONArray(STRING_ARRAY_KEY, jsonObject);

    String actualString = parser.getString(-1, jsonArray);

    assertNull(actualString);
  }

  @Test
  public void testGetString_ArrayWithIndex_ValueIsNotString() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString_WithArray());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray jsonArray = parser.getJSONArray(STRING_ARRAY_KEY, jsonObject);

    String actualString = parser.getString(jsonArray.length(), jsonArray);

    assertNull(actualString);
  }

  @Test
  public void testGetJSONArray() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray actualJSONArray = parser.getJSONArray(MAP_KEY, jsonObject);

    assertEquals(3, actualJSONArray.length());

    final JSONObject actualText0 = (JSONObject) actualJSONArray.get(0);

    assertTrue(parser.hasKey(ARRAY_TEXT0_KEY, actualText0));
    final String text0 = actualText0.getString(ARRAY_TEXT0_KEY);

    assertEquals(TEXT0, text0);

    final JSONObject actualText1 = (JSONObject) actualJSONArray.get(1);

    assertTrue(parser.hasKey(ARRAY_TEXT1_KEY, actualText1));
    final String text1 = actualText1.getString(ARRAY_TEXT1_KEY);

    assertEquals(TEXT1, text1);

    final JSONObject actualText2 = (JSONObject) actualJSONArray.get(2);

    assertTrue(parser.hasKey(ARRAY_TEXT2_KEY, actualText2));
    final String text2 = actualText2.getString(ARRAY_TEXT2_KEY);

    assertEquals(TEXT2, text2);
  }

  @Test
  public void testGetJSONArray_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray actualJSONArray = parser.getJSONArray(MAP_KEY + "FOO_BAR", jsonObject);

    assertEquals(0, actualJSONArray.length());
  }

  @Test
  public void testGetJSONArray_ValueIsNotAnArray() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray actualJSONArray = parser.getJSONArray(LONG_KEY, jsonObject);

    assertEquals(0, actualJSONArray.length());
  }

  @Test
  public void testGetJSONObject_FromJSONObject() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONObject actualJSONObject = parser.getJSONObject(OBJECT_KEY, jsonObject);

    assertEquals(OBJECT_TEXT_VALUE, actualJSONObject.getString(OBJECT_TEXT_KEY));
  }

  @Test
  public void testGetJSONObject_FromJSONObject_KeyDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONObject actualJSONObject = parser.getJSONObject(OBJECT_KEY + "FOO_BAR", jsonObject);

    assertNull(actualJSONObject);
  }

  @Test
  public void testGetJSONObject_FromJSONObject_ValueIsNotAnObject() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONObject actualJSONObject = parser.getJSONObject(LONG_KEY, jsonObject);

    assertNull(actualJSONObject);
  }

  @Test
  public void testGetJSONObject_FromJSONObject_WithIndex() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONArray actualJSONArray = parser.getJSONArray(MAP_KEY, jsonObject);

    assertEquals(TEXT0, parser.getString(ARRAY_TEXT0_KEY, parser.getJSONObject(actualJSONArray, 0)));
    assertEquals(TEXT1, parser.getString(ARRAY_TEXT1_KEY, parser.getJSONObject(actualJSONArray, 1)));
    assertEquals(TEXT2, parser.getString(ARRAY_TEXT2_KEY, parser.getJSONObject(actualJSONArray, 2)));
  }

  @Test
  public void testGetJSONObject_FromJSONObject_WithIndex_IndexDoesNotExist() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONObject actualJSONObject = parser.getJSONObject(OBJECT_KEY + "FOO_BAR", jsonObject);

    assertNull(actualJSONObject);
  }

  @Test
  public void testGetJSONObject_FromJSONObject_WithIndex_ValueIsNotAnObject() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    final JSONObject actualJSONObject = parser.getJSONObject(LONG_KEY, jsonObject);

    assertNull(actualJSONObject);
  }

  @Test
  public void testHasKey() throws JSONException {
    final JSONObject jsonObject = new JSONObject(getJsonString());
    final AbstractParser parser = new MockAbstractParser();

    assertTrue(parser.hasKey(LONG_KEY, jsonObject));
    assertFalse(parser.hasKey(LONG_KEY + "FOO_BAR", jsonObject));
    assertFalse(parser.hasKey(LONG_KEY + "FOO_BAR", null));
  }

  private String getJsonString() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"," +
           "\"" + BOOLEAN_KEY + "\":" + BOOLEAN.toString() + "," +
           "\"" + DOUBLE_KEY + "\":" + DOUBLE + "," +
           "\"" + INTEGER_KEY + "\":" + INTEGER + "," +
           "\"" + LONG_KEY + "\":" + LONG + "," +
           "\"" + STRING_KEY + "\":" + STRING + "," +
           "\"" + OBJECT_KEY + "\":{\"" + OBJECT_TEXT_KEY + "\":\"" + OBJECT_TEXT_VALUE + "\"}," +
           "\"" + MAP_KEY + "\": [" +
           "{\"" + ARRAY_TEXT0_KEY + "\": \"" + TEXT0 + "\"}," +
           "{\"" + ARRAY_TEXT1_KEY + "\": \"" + TEXT1 + "\"}," +
           "{\"" + ARRAY_TEXT2_KEY + "\": \"" + TEXT2 + "\"}]}";
  }

  private String getJsonString_BooleanIsNonStandard() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"," +
           "\"" + BOOLEAN_KEY + "\":\"" + BOOLEAN_NON_STANDARD + "\"}";
  }

  private String getJsonString_WithArray() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"," +
           "\"" + STRING_ARRAY_KEY + "\": [" +
           "\"" + TEXT0 + "\"," +
           "\"" + TEXT1 + "\"," +
           "\"" + TEXT2 + "\"]," +
           "\"" + NON_STRING_ARRAY_KEY + "\": [" + LONG + "]}";
  }

  /**
   * Header, no language.
   */
  private String getJsonString_NoLanguage() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"}";
  }

  /**
   * Header, no status.
   */
  private String getJsonString_NoStatus() {
    return "{\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"}";
  }

  /**
   * Header, no text.
   */
  private String getJsonString_NoText() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"}";
  }

  /**
   * Header, no usage.
   */
  private String getJsonString_NoUsage() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_URL + "\": \"" + URL + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"}";
  }

  /**
   * Header, no url.
   */
  private String getJsonString_NoUrl() {
    return "{\"" + JSONConstants.RESULTS_STATUS + "\":\"" + STATUS_STRING + "\"," +
           "\"" + JSONConstants.RESULTS_STATUS_INFO + "\": \"" + STATUS_INFO + "\"," +
           "\"" + JSONConstants.RESULTS_USAGE + "\": \"" + USAGE + "\"," +
           "\"" + JSONConstants.RESULTS_LANGUAGE + "\": \"" + LANGUAGE + "\"," +
           "\"" + JSONConstants.RESULTS_TEXT + "\": \"" + TEXT + "\"}";
  }

  class MockAbstractParser extends AbstractParser {
    @Override
    protected void populateResponse(final Response response) {
    }
  }
}
