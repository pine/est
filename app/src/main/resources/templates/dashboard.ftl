<#ftl strip_whitespace=true>
<#-- @ftlvariable name="items" type="java.util.List<moe.pine.est.models.ViewLog>" -->

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>Est Dashboard</title>
</head>
<body>

<#list items as item>
  ${item.dt}
  ${item.hash}
</#list>

</body>
</html>
