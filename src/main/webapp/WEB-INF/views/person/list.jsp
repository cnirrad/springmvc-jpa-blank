<c:import url="/WEB-INF/views/common/layout.jsp" charEncoding="UTF-8">
    <c:param name="title" value="PERSON LIST" />
    <c:param name="body">
    	<link rel="stylesheet" type="text/css" 
    		href="${contextRoot}/js/dojox/grid/enhanced/resources/claro/EnhancedGrid.css" />
		<link rel="stylesheet" type="text/css" 
			href="${contextRoot}/js/dojox/grid/enhanced/resources/EnhancedGrid_rtl.css" />
			
        <p>
            <a href='${contextRoot}/person/form/' class="btn btn-info">CREATE</a>
        </p>
        <script>
        	require(["dojox/grid/EnhancedGrid", 
        	         "dojo/store/JsonRest",
        	         "dojo/data/ObjectStore",
        	         "dojox/grid/enhanced/plugins/Pagination",
        	         "dojo/domReady!"],
        		function(EnhancedGrid, JsonRest, ObjectStore) {
        			var jsonRest = new JsonRest({target: "${contextRoot}/person/query"});
        			var store = new ObjectStore({objectStore: jsonRest});
        		
        			var grid = new EnhancedGrid({
        				store: store,
        				query: {id: "*"},
        				autoHeight: 10,
        				autoWidth: true,
        				plugins: {
        					pagination: true
        				},        				
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

