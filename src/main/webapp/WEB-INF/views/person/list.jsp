<c:import url="/WEB-INF/views/common/layout.jsp" charEncoding="UTF-8">
    <c:param name="title" value="PERSON LIST" />
    <c:param name="body">
    	<link rel="stylesheet" type="text/css" 
    		href="${contextRoot}/js/dojox/grid/resources/Grid.css" />
		<link rel="stylesheet" type="text/css" 
			href="${contextRoot}/js/dojox/grid/resources/claroGrid.css" />
			
        <p>
            <a href='${contextRoot}/person/form/' class="btn btn-info">CREATE</a>
        </p>
        <script>
        	require(["dojox/grid/DataGrid", 
        	         "dojo/store/JsonRest",
        	         "dojo/data/ObjectStore",
        	         "dojo/domReady!"],
        		function(DataGrid, JsonRest, ObjectStore) {
        			var jsonRest = new JsonRest({target: "${contextRoot}/person/query"});
        			var store = new ObjectStore({objectStore: jsonRest});
        		
        			var grid = new DataGrid({
        				store: store,
        				query: {id: "*"},
        				autoHeight: 10,
        				autoWidth: true,
        				structure: [
        				        { name: "Id", field: "id"},
        				        { name: "Name", field: "name"},
        				        { name: "Age", field: "age"}
        				    ]
        			}, "personList");
        			grid.startup();	
        	});
        </script>
        
        <div id="personList" ></div>
        
        
    </c:param>
</c:import>

