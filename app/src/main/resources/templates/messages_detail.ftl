<#ftl strip_whitespace=true>
<#-- @ftlvariable name="layout" type="moe.pine.est.models.ViewLayout" -->
<#-- @ftlvariable name="log" type="moe.pine.est.models.ViewLog" -->

<#import "base.ftl" as base>

<@base.page layout=layout>
  <div class="container">
    <table class="table table-striped">
      <tr>
        <th>Recipient</th>
        <td>${log.messageLog.recipient?html}</td>
      </tr>
      <tr>
        <th>Sender</th>
        <td>${log.messageLog.sender?html}</td>
      </tr>
      <tr>
        <th>From</th>
        <td>${log.messageLog.from?html}</td>
      </tr>
      <tr>
        <th>Subject</th>
        <td>${log.messageLog.subject?html}</td>
      </tr>
      <tr>
        <th>Body plain</th>
        <td>${log.messageLog.bodyPlain?html?replace('\r?\n', '<br>')}</td>
      </tr>
      <tr>
        <th>Stripped text</th>
        <td>${log.messageLog.strippedText?html}</td>
      </tr>
      <tr>
        <th>Stripped signature</th>
        <td>${log.messageLog.strippedSignature?html}</td>
      </tr>
      <tr>
        <th>Body HTML</th>
        <td>${log.messageLog.bodyHtml?html}</td>
      </tr>
      <tr>
        <th>Stripped HTML</th>
        <td>${log.messageLog.strippedHtml?html}</td>
      </tr>
      <tr>
        <th>Date</th>
        <td>${log.messageLog.timestamp}</td>
      </tr>
      <tr>
        <th>Token</th>
        <td>${log.messageLog.token}</td>
      </tr>
    </table>
  </div>
</@base.page>
