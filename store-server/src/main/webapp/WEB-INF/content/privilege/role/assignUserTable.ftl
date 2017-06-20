<#list users as user>
<tr class=" ${(user_index%2==0)?string("gradeA even","gradeU odd")}">
    <td><input class="check-all" type="checkbox" name="userId"  value="${user.id?c}"></td>
    <td>${user.loginName}</td>
    <td>${user.trueName!""}</td>
    <td>${user.email!""}</td>
</tr>
</#list>
