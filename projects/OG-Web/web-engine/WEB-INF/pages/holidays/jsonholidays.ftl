<#escape x as x?html>
{
    "header": {
        "type": "Holidays",
        <#if searchResult??>
        "pgIdx": ${"${paging.firstItem}"?replace(',','')},
        "pgSze": ${"${paging.pagingSize}"?replace(',','')},
        "pgTtl": ${"${paging.totalItems}"?replace(',','')},
	      </#if>
	      "dataFields": ["id","name" <#if searchRequest.type = ''>, "type"</#if> ,"validFrom"]
    },
    "data" : [<#if searchResult??>
      <#list searchResult.documents as item>
	       "${item.uniqueId.objectId}|${item.name}<#if searchRequest.type = ''>|${item.holiday.type}</#if>|${item.versionFromInstant}"<#if item_has_next>,</#if>
	    </#list> </#if>]        
}
</#escape>