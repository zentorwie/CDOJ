<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="cdoj" uri="/WEB-INF/cdoj.tld" %>
<%--
  ~ /*
  ~  * cdoj, UESTC ACMICPC Online Judge
  ~  * Copyright (c) 2013 fish <@link lyhypacm@gmail.com>,
  ~  * 	mzry1992 <@link muziriyun@gmail.com>
  ~  *
  ~  * This program is free software; you can redistribute it and/or
  ~  * modify it under the terms of the GNU General Public License
  ~  * as published by the Free Software Foundation; either version 2
  ~  * of the License, or (at your option) any later version.
  ~  *
  ~  * This program is distributed in the hope that it will be useful,
  ~  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  * GNU General Public License for more details.
  ~  *
  ~  * You should have received a copy of the GNU General Public License
  ~  * along with this program; if not, write to the Free Software
  ~  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
  ~  */
  --%>

<%--
  Created by IntelliJ IDEA.
  User: mzry1992
  Date: 13-2-8
  Time: 下午1:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="<s:url value="/plugins/uploadify/uploadify.css"/>" rel="stylesheet">
    <script src="<s:url value="/plugins/uploadify/jquery.uploadify.js"/>"></script>
    <script src="<s:url value="/scripts/cdoj.admin.problemDataAdmin.js"/>"></script>
    <title>Problem</title>
</head>
<body>
<div class="row" id="problemEditor">

    <div class="span10">
        <form class="form-horizontal">
            <fieldset>
                <legend>Problem <span id="problemId">${targetProblemId}</span></legend>
                <div class="row">
                    <div class="span4">
                        <div class="control-group">
                            <label class="control-label">Time limit</label>
                            <div class="controls">
                                <div class="input-append">
                                    <input type="text"
                                           name="problemDTO.timeLimit"
                                           maxlength="6"
                                           value="${problemDataView.timeLimit}"
                                           id="problemDTO_timeLimit"
                                           class="span1">
                                    <span class="add-on">ms</span>
                                </div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">Memory limit</label>
                            <div class="controls">
                                <div class="input-append">
                                    <input type="text"
                                           name="problemDTO.memoryLimit"
                                           maxlength="10"
                                           value="${problemDataView.memoryLimit}"
                                           id="problemDTO_memoryLimit"
                                           class="span2">
                                    <span class="add-on">KB</span>
                                </div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">Output limit</label>
                            <div class="controls">
                                <div class="input-append">
                                    <input type="text"
                                           name="problemDTO.outputLimit"
                                           maxlength="10"
                                           value="${problemDataView.outputLimit}"
                                           id="problemDTO_outputLimit"
                                           class="span2">
                                    <span class="add-on">KB</span>
                                </div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">Is SPJ</label>
                            <div class="controls">
                                <label class="radio inline">
                                    <input type="radio"
                                           name="problemDTO.isSpj"
                                           value="true"
                                           <s:if test="problemDataView.isSpj == true"/>checked="true">
                                    Yes
                                </label>
                                <label class="radio inline">
                                    <input type="radio"
                                           name="problemDTO.isSpj"
                                           value="false"
                                           <s:if test="problemDataView.isSpj == false"/>checked="true">
                                    No
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="span4">
                        <div class="control-group">
                            <label class="control-label">Java time limit</label>
                            <div class="controls">
                                <div class="input-append">
                                    <input type="text"
                                           name="problemDTO.javaTimeLimit"
                                           maxlength="6"
                                           value="${problemDataView.javaTimeLimit}"
                                           id="problemDTO_javaTimeLimit"
                                           class="span1">
                                    <span class="add-on">ms</span>
                                </div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">Java memory limit</label>
                            <div class="controls">
                                <div class="input-append">
                                    <input type="text"
                                           name="problemDTO.javaMemoryLimit"
                                           maxlength="6"
                                           value="${problemDataView.javaMemoryLimit}"
                                           id="problemDTO_javaMemoryLimit"
                                           class="span2">
                                    <span class="add-on">KB</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
            <fieldset>
                <legend>Problem data</legend>
                <div class="control-group">
                    <label class="control-label">Current data count</label>
                    <div class="controls">
                        <input type="text"
                               name="problemDTO.javaMemoryLimit"
                               maxlength="6"
                               value="${problemDataView.dataCount}"
                               id="problemDTO_dataCount"
                               class="span1"
                               readonly="true">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">Upload data file</label>
                    <div class="controls">
                        <input type="file"
                               id="problemDataUploader">
                        <span class="help-inline">Please use zip to package your files.</span>
                    </div>
                </div>

                <div class="form-actions">
                    <s:submit name="submit"
                              cssClass="btn btn-primary"
                              value="Submit"
                              theme="bootstrap"/>
                </div>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>