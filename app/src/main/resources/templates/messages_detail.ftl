<#ftl strip_whitespace=true>
<#-- @ftlvariable name="layout" type="moe.pine.est.models.ViewLayout" -->
<#-- @ftlvariable name="log" type="moe.pine.est.models.ViewLog" -->

<#import "base.ftl" as base>

<@base.page layout=layout>
  <div class="container">
    <table class="table table-striped">
      <tr>
        <th>ID</th>
        <td>${dt}:${hash}</td>
      </tr>
      <tr>
        <th>Sender</th>
        <td>${log.messageLog.sender}</td>
      </tr>
      <tr>
        <th>Subject</th>
        <td>${log.messageLog.subject}</td>
      </tr>
      <tr>
        <th>Date</th>
        <td>${log.messageLog.timestamp}</td>
      </tr>
    </table>
  </div>
</@base.page>
