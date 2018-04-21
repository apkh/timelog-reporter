<!DOCTYPE html>

<html>

  <head>
    <script   src="https://code.jquery.com/jquery-2.2.4.min.js"   integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="   crossorigin="anonymous"></script>
    <script   src="https://code.jquery.com/ui/1.12.0/jquery-ui.min.js"   integrity="sha256-eGE6blurk5sHj+rmkfsGYeKyZx3M4bG+ZlFyA7Kns7E="   crossorigin="anonymous"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/tabulator/3.5.0/css/tabulator.min.css" rel="stylesheet">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/tabulator/3.5.0/js/tabulator.min.js"></script>

  </head>
  <body>
    <div id="example-table"></div>

    <script type="text/javascript">

 Tabulator.extendExtension("ajax", "defaultConfig", {
        type:"GET",
        contentType : "application/json; charset=utf-8"
    });

    $("#example-table").tabulator({
      height:"600px",
      layout:'fitColumns',
      clipboard:true, //enable clipboard functionality
      clipboardCopySelector:"table",

      columns:[
        {title:"Key", field:"id",  sortable:true, width:100},
        {title:"Summary", field:"description", sortable:false},
        {title:"Assignee", field:"assignee", sortable:true},
        {title:"Status", field:"status", sortable:true},
        {title:"Workload", field:"workload", sortable:true, sorter:"number"},
      ],
    rowClick:function(e, row){
        window.location.href = "http://jira.brokerkf.ru/browse/" + row.getIndex()
    },

    });
    $("#example-table").tabulator("setData","http://localhost:8080/tasks-table");

    $(window).resize(function(){
      $("#example-table").tabulator("redraw");
    });

console.info("started")
    </script>
      </body>

</html>
