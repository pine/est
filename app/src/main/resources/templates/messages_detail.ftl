<#ftl strip_whitespace=true>
<#-- @ftlvariable name="layout" type="moe.pine.est.models.ViewLayout" -->
<#-- @ftlvariable name="log" type="moe.pine.est.models.ViewLog" -->

<#import "base.ftl" as base>

<@base.page layout=layout>
  <div class="container">
    <table class="table table-striped">
      <tr>
        <th>Recipient</th>
        <td>${log.messageLog.recipient}</td>
      </tr>
      <tr>
        <th>Sender</th>
        <td>${log.messageLog.sender}</td>
      </tr>
      <tr>
        <th>From</th>
        <td>${log.messageLog.from}</td>
      </tr>
      <tr>
        <th>Subject</th>
        <td>${log.messageLog.subject}</td>
      </tr>
      <tr>
        <th>Body plain</th>
        <td>${log.messageLog.bodyPlain}</td>
      </tr>
      <tr>
        <th>Stripped text</th>
        <td>${log.messageLog.strippedText}</td>
      </tr>
      <tr>
        <th>Stripped signature</th>
        <td>${log.messageLog.strippedSignature}</td>
      </tr>
      <tr>
        <th>Body HTML</th>
        <td>${log.messageLog.bodyHtml}</td>
      </tr>
      <tr>
        <th>Stripped HTML</th>
        <td>${log.messageLog.strippedHtml}</td>
      </tr>
      <tr>
        <th>Date</th>
        <td>${log.messageLog.timestamp}</td>
      </tr>
    </table>
  </div>
</@base.page>
