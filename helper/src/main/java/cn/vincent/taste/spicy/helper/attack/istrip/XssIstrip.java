/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.helper.attack.istrip;

import java.util.regex.Pattern;

/**
 * XSS脚本
 *
 * @author vincent
 * @version 1.0 2017/8/20 23:44
 */
public class XssIstrip implements Istrip {

    private static final Pattern SCRIPT_TAGS_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_SRC_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_SRC2_PATTERN = Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_TAGSEND_PATTERN = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_TAGS2_PATTERN = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_EVAL_PATTERN = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_EXPRESSION_PATTERN = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_VBSCRIPT_PATTERN = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_ONLOAD_PATTERN = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    public String strip(String value) {
        String rlt = null;

        if (value != null) {
            // Avoid null characters
            rlt = value.replaceAll("", "");

            // Avoid anything between script tags
            rlt = SCRIPT_TAGS_PATTERN.matcher(rlt).replaceAll("");

            // Avoid anything in a src='...' type of expression
            rlt = SCRIPT_SRC_PATTERN.matcher(rlt).replaceAll("");

            rlt = SCRIPT_SRC2_PATTERN.matcher(rlt).replaceAll("");

            // Remove any lonesome </script> tag
            rlt = SCRIPT_TAGSEND_PATTERN.matcher(rlt).replaceAll("");

            // Remove any lonesome <script ...> tag
            rlt = SCRIPT_TAGS2_PATTERN.matcher(rlt).replaceAll("");

            // Avoid eval(...) expressions
            rlt = SCRIPT_EVAL_PATTERN.matcher(rlt).replaceAll("");

            // Avoid expression(...) expressions
            rlt = SCRIPT_EXPRESSION_PATTERN.matcher(rlt).replaceAll("");

            // Avoid javascript:... expressions
            rlt = SCRIPT_JAVASCRIPT_PATTERN.matcher(rlt).replaceAll("");

            // Avoid vbscript:... expressions
            rlt = SCRIPT_VBSCRIPT_PATTERN.matcher(rlt).replaceAll("");

            // Avoid onload= expressions
            rlt = SCRIPT_ONLOAD_PATTERN.matcher(rlt).replaceAll("");
        }

        return rlt;
    }
}
