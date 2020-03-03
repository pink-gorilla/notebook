with import <nixpkgs> {};

stdenv.mkDerivation rec {
  name = "python-environment";

  buildInputs = [ pkgs.python36 pkgs.python36Packages.flask pkgs.ffmpeg ];

  shellHook = ''
    export FLASK_DEBUG=1
    export FLASK_APP="main.py"

    export API_KEY="some secret key"
  '';
}
