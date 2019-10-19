// stolen from: https://stackoverflow.com/questions/16228345/requirejs-load-string
// javascript ui components define their source in a string, 
// they do not get delivered via a URL get. 
// This defines the plugin to load a module from a string.

define("loadstring",[], function(){
    var strings=[],
        re_package_name = /^string_module_(\d+)$/;
    return {
        normalize: function(name, _){
            if(re_package_name.test(name)){
                return name
            }
            var nml = "string_module_" + (strings.push(name)-1);
            return nml;
        },
        load: function(name, _, onLoad, config){
            if(re_package_name.test(name)){
                onLoad.fromText(strings[name.match(re_package_name)[1]]);
            }else{
                onLoad.error("Invalid package name: ",name);
            }
        }  
    }
});