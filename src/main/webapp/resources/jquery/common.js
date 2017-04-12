function checkLangExists(locale){
	var arr = null;
	var languageCodes =  $.trim($("#langCodes").val());
	arr = languageCodes.split(',');
	return $.inArray( $.trim(locale),arr ); 
}

function removeLangShorName(languageDisplayName){
	if(languageDisplayName == ""){
		return languageDisplayName;
	}
	return languageDisplayName.replace(/ *\[[^\]]*]/, '');
}	