<#ftl strip_whitespace=true>
<#-- @ftlvariable name="layout" type="moe.pine.est.models.ViewLayout" -->
<#-- @ftlvariable name="logs" type="java.util.List<moe.pine.est.models.ViewLog>" -->

<#import "base.ftl" as base>

<@base.page layout=layout>
  <div class="container">
    <table class="table table-striped">
      <thead>
      <tr>
        <th>Subject</th>
        <th>From</th>
        <th>Date</th>
      </tr>
      </thead>
      <tfoot>
      </tfoot>
      <tbody>
      <#list logs as log>
        <tr>
          <td><a href="${log.path}" rel="bookmark">${log.messageLog.subject}</a></td>
          <td>${log.messageLog.from?html}</td>
          <td>${log.messageLog.timestamp}</td>
        </tr>
      </#list>
      </tbody>
    </table>
  </div>
</@base.page>
