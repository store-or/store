<table id="action_table"  cellpadding="0" cellspacing="0" border="0"
       class="table table-striped table-bordered" aria-describedby="jq-datatables-example_info">
    <tbody>
    <#list tree as action>
        <#if action_index gt 0 >
            <#if action.parentName != "/">
            <tr data-tt-parent-id="${action.parentName}" data-tt-id="${action.name}" class="${(action_index%2==0)?string("gradeA even","gradeU odd")} child-of-tr-${action.parentName}" id="tr-${action.name}">
                <td><input class="check-all child-of-ac_ck_${action.parentName}" id="ac_ck_${action.name}"  type="checkbox" <#if action.assign>checked="true"</#if>  name="action" value="${action.name}"/>${action.description!""}</td>
                <td>${action.name!""}</td>
            </tr>
            <#else>
            <tr data-tt-id="${action.name}" class="${(action_index%2==0)?string("gradeA even","gradeU odd")} ignore" id="tr-${action.name}">
                <td><input class="check-all" style="float: none"  id="ac_ck_${action.name}"  type="checkbox"  /><span style="color:olive">${action.description!""}</span></td>
                <td></td>
            </tr>
            </#if>
        </#if>
    </#list>
    </tbody>
</table>