
//Auto complete no input
$("#especialidade").autocomplete({
    source : function(request, response) { //Json que vai e outro voltando na resposta
        $.ajax({
            method: "GET",
            url: "/especialidades/titulo",
            data: {
                termo: request.term
            },
            success: function(data){
                response(data);
            }
        });
    }
});

/**
 * após a especialidade ser selecionado busca
 * os médicos referentes e os adiciona na página com
 * radio
 */
$('#especialidade').on('blur', function() { //Ao perder o focus do elemento
    $('div').remove(".custom-radio");
	var titulo = $(this).val();
	if ( titulo != '' ) {
	    //Requisição get e capturando o retorno, trás uma lista de médicos
		$.get( "/medicos/especialidade/titulo/" + titulo , function( result ) {

			var ultimo = result.length - 1;

			$.each(result, function (k, v) {

				if ( k == ultimo ) {
	    			$("#medicos").append(
	    				 '<div class="custom-control custom-radio">'
	    				+  '<input class="custom-control-input" type="radio" id="customRadio'+ k +'" name="medico.id" value="'+ v.id +'" required>'
						+  '<label class="custom-control-label" for="customRadio'+ k +'">'+ v.nome +'</label>'
						+  '<div class="invalid-feedback">Médico é obrigatório</div>'
						+'</div>'
	    			);
				} else {
	    			$("#medicos").append(
	    				 '<div class="custom-control custom-radio">'
	    				+  '<input class="custom-control-input" type="radio" id="customRadio'+ k +'" name="medico.id" value="'+ v.id +'" required>'
						+  '<label class="custom-control-label" for="customRadio'+ k +'">'+ v.nome +'</label>'
						+'</div>'
	        		);
				}
		    });
		});
	}
});

/**
 * busca os horários livres para consulta conforme a data e o médico.
 * os horários são adicionados a página como um select:option.
*/
$('#data').on('blur', function () {
	$("#horarios").empty();
    var data = $(this).val();
    var medico = $('input[name="medico.id"]:checked').val(); //input selecionado
    if (!Date.parse(data)) { //verifica se a data foi devidamente selecionada
        console.log('data nao selecionada')
    } else {
    	$.get('/agendamentos/horario/medico/'+ medico + '/data/' + data , function( result ) {
    		$.each(result, function (k, v) {
    			$("#horarios").append(
    				'<option class="op" value="'+ v.id +'">'+ v.horaMinuto + '</option>'
    			);
    	    });
    	});
    }
});
