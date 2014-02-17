<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
    <script src="/admin/js/boot.js"></script>
    <script src="/admin/js/highcharts/highcharts.js"></script>
    <link href="/admin/css/wuliu.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
    .header
    {
        background:url(/admin/images/header.gif) repeat-x 0 -1px;
    }
    </style>
    
 </head>
<body>
<!--Layout-->
<div id="layout1" class="mini-layout" style="width:100%;height:100%;">
    <div title="center" region="center" bodyStyle="overflow:hidden;" style="border:0;">
	     <form id="form1" method="post">
	        <input name="id" class="mini-hidden" />
	        <div style="padding-left:11px;padding-bottom:5px;">
	            <table style="table-layout:fixed;">
	                <tr>
	                    <td style="width:70px;">开始日期：</td>
	                    <td style="width:150px;">    
	                        <input id="start" name="start" class="mini-datepicker"  format="yyyy-MM-dd"/>
	                    </td>
	                    <td style="width:70px;">结束日期：</td>
	                    <td style="width:150px;">    
	                        <input id="end" name="end" class="mini-datepicker" format="yyyy-MM-dd"/>
	                    </td>
	                    <td>
	                    	<a class="mini-button" onclick="onOk" style="width:60px;margin-right:20px;">统计</a>       
	                    </td>
	                </tr>
	            </table>
	        </div>
	        <div style="text-align:center;padding:10px;">               
	              
	        </div>        
	    </form>
      <div id="container" style="min-width: 400px; height: 600px; margin: 0 auto"></div>
    </div>
</div>

    

</body>
</html>
<script type="text/javascript">
        mini.parse();

		$(function () {
        $('#container').highcharts({
            chart: {
                type: 'bar'
            },
            title: {
                text: '下单金额统计年报表'
            },
            xAxis: {
                categories: [
                <#list list as record>
		    		'${record.count_day}',
		    	</#list>
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: '下单金额'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                },
                 bar: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            series: [{
                name: '下单金额',
                data: [
                 <#list list as record>
		    		${record.num},
		    	</#list>
                ]
    
            }]
        });
    });
    
    function onOk(){
    	var start = mini.get("start").getFormValue();
    	var end = mini.get("end").getFormValue();
    	window.location = "?start=" + start + "&end=" + end;
    }
    </script>
