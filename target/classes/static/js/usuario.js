//datatables - lista de médicos
$(document).ready(function() {
	moment.locale('pt-BR');
	var table = $('#table-usuarios').DataTable({
		searching : true,
		lengthMenu : [ 5, 10 ],
		processing : true,
		serverSide : true,
		responsive : true,
		ajax : {
			url : '/u/datatables/server/usuarios',
			data : 'data'
		},
		columns : [
				{data : 'id'},
				{data : 'email'},
				{	data : 'ativo', 
					render : function(ativo) {
						return ativo == true ? 'Sim' : 'Não';
					}
				},
				{	data : 'perfis',									 
					render : function(perfis) {
						var aux = new Array();
						$.each(perfis, function( index, value ) {
							  aux.push(value.desc);
						});
						return aux;
					},
					orderable : false,
				},
				{	data : 'id',	
					render : function(id) {
						return ''.concat('<a class="btn btn-success btn-sm btn-block"', ' ')
								 .concat('href="').concat('/u/editar/credenciais/usuario/').concat(id, '"', ' ') 
								 .concat('role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
								 .concat('<i class="fas fa-edit"></i></a>');
					},
					orderable : false
				},
				{	data : 'id',	
					render : function(id) {
						return ''.concat('<a class="btn btn-info btn-sm btn-block"', ' ') 
								 .concat('id="dp_').concat(id).concat('"', ' ') 
								 .concat('role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
								 .concat('<i class="fas fa-edit"></i></a>');
					},
					orderable : false
				}
		]
	});

	//Prepara href para btn de dados pessoais
	//Captura o click no btn com isso seleciona a linha do click
	//Armazenas as colunas que tem na linha
	$("#table-usuarios tbody").on("click", "[id*='dp_']", function(){
	    var row = table.row($(this).parents("tr")).data();
	    var aux = new Array();
	    $.each(row.perfis, function(k, v){
	        aux.push(v.id);
	    });
	    document.location.href = "/u/editar/dados/usuario/" + row.id + "/perfis/" + aux;
	});

});

//Verifica se os campos de input da nova senha estão iguais
$('.pass').keyup(function(){
    if($('#senha1').val() == "" || $('#senha2').val() == ""){
        $('#senha3').val("");
        $('#senha3').attr('readonly', 'readonly');
    }else{
        $('#senha1').val() === $('#senha2').val()
                ? $('#senha3').removeAttr('readonly')
                : $('#senha3').attr('readonly', 'readonly');
    }
});