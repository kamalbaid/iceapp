<%
// New location to be redirected
String licenseURL = request.getContextPath() + "/logout";
response.sendRedirect(licenseURL);
%>