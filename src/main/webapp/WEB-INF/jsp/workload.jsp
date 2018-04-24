<!DOCTYPE html>

<html>

  <head>
    <script   src="https://code.jquery.com/jquery-2.2.4.min.js"   integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="   crossorigin="anonymous"></script>
    <script   src="https://code.jquery.com/ui/1.12.0/jquery-ui.min.js"   integrity="sha256-eGE6blurk5sHj+rmkfsGYeKyZx3M4bG+ZlFyA7Kns7E="   crossorigin="anonymous"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/tabulator/3.5.0/css/tabulator.min.css" rel="stylesheet">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/tabulator/3.5.0/js/tabulator.min.js"></script>

  </head>
  <body>

  <h1> workload table </h1>
    <div id="example-table"></div>

    <script type="text/javascript">

 Tabulator.extendExtension("ajax", "defaultConfig", {
        type:"GET",
        contentType : "application/json; charset=utf-8"
    });

    var href = window.location.href;
    var baseURL = href.substr(0,href.indexOf(window.location.pathname));



$.ajax({
  url: baseURL + "/wl-header",
  cache: false,
  dataType: "json",
  success: function(header){
    $("#example-table").tabulator({
      height:"600px",
      layout:'fitColumns',
      clipboard:true, //enable clipboard functionality
      clipboardCopySelector:"table",

      columns:header,
    });
    $("#example-table").tabulator("setData",baseURL + "/wl-table");

    $(window).resize(function(){
      $("#example-table").tabulator("redraw");
    });
  }
});

console.info("started")
    </script>
      </body>

</html>
