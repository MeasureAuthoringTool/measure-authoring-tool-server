<h3><a name="d1e767" href="#toc">Risk Adjustment Variables</a></h3>
<ul style="list-style:none;padding-left: 25px;">
	
	<#if model.riskAdjustmentVariables?has_content>
		<#list model.riskAdjustmentVariables as expression>
			<#include "expression.ftl" />
		</#list>
		<#else>
		<li>None</li>
	</#if>
</ul>