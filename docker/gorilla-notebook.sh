#!/bin/sh

# -----------------------------------------------------------------------------
# Environment Variable Prerequisites
#
#   JAVA_HOME       Must point at your Java Development Kit installation.
#                   Required to run the with the "debug" argument.
#
#   JRE_HOME        Must point at your Java Runtime installation.
#                   Defaults to JAVA_HOME if empty. If JRE_HOME and JAVA_HOME
#                   are both set, JRE_HOME is used.
#
#   JAVA_OPTS       (Optional) Java runtime options used when any command
#                   is executed.
#
#   JPDA_OPTS       (Optional) Java runtime options used when the "jpda start"
#                   command is executed. If used, JPDA_TRANSPORT, JPDA_ADDRESS,
#                   and JPDA_SUSPEND are ignored. Thus, all required jpda
#                   options MUST be specified. The default is:
#
# -----------------------------------------------------------------------------

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
darwin=false
os400=false
case "$(uname)" in
CYGWIN*) cygwin=true ;;
Darwin*) darwin=true ;;
OS400*) os400=true ;;
esac

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' >/dev/null; then
    PRG="$link"
  else
    PRG=$(dirname "$PRG")/"$link"
  fi
done

# Get standard environment variables
PRGDIR=$(dirname "$PRG")

# Only set GORILLA_HOME if not already set
[ -z "$GORILLA_HOME" ] && GORILLA_HOME=$(
  cd "$PRGDIR/.." >/dev/null
  pwd
)

# Copy GORILLA_BASE from GORILLA_HOME if not already set
[ -z "$GORILLA_BASE" ] && GORILLA_BASE="$GORILLA_HOME"

# Get standard Java environment variables
# Make sure prerequisite environment variables are set
if [ -z "$JAVA_HOME" -a -z "$JRE_HOME" ]; then
  if $darwin; then
    # Bugzilla 54390
    if [ -x '/usr/libexec/java_home' ]; then
      export JAVA_HOME=$(/usr/libexec/java_home)
    # Bugzilla 37284 (reviewed).
    elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
      export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
    fi
  else
    JAVA_PATH=$(which java 2>/dev/null)
    if [ "x$JAVA_PATH" != "x" ]; then
      JAVA_PATH=$(dirname $JAVA_PATH 2>/dev/null)
      JRE_HOME=$(dirname $JAVA_PATH 2>/dev/null)
    fi
    if [ "x$JRE_HOME" = "x" ]; then
      # XXX: Should we try other locations?
      if [ -x /usr/bin/java ]; then
        JRE_HOME=/usr
      fi
    fi
  fi
  if [ -z "$JAVA_HOME" -a -z "$JRE_HOME" ]; then
    echo "Neither the JAVA_HOME nor the JRE_HOME environment variable is defined"
    echo "At least one of these environment variable is needed to run this program"
    exit 1
  fi
fi

if [ -z "$JRE_HOME" ]; then
  JRE_HOME="$JAVA_HOME"
fi

# Don't override the endorsed dir if the user has set it previously
#if [ -z "$JAVA_ENDORSED_DIRS" ]; then
#  # Set the default -Djava.endorsed.dirs argument
#  JAVA_ENDORSED_DIRS="$CATALINA_HOME"/endorsed
#fi

# Set standard commands for invoking Java, if not already set.
if [ -z "$_RUNJAVA" ]; then
  _RUNJAVA="$JRE_HOME"/bin/java
fi
if [ "$os400" != "true" ]; then
  if [ -z "$_RUNJDB" ]; then
    _RUNJDB="$JAVA_HOME"/bin/jdb
  fi
fi

# Ensure that any user defined CLASSPATH variables are not used on startup,
# but allow them to be specified in setenv.sh, in rare case when it is needed.
CLASSPATH=

#if [ -r "$GORILLA_BASE/bin/setenv.sh" ]; then
#  . "$GORILLA_BASE/bin/setenv.sh"
#elif [ -r "$GORILLA_HOME/bin/setenv.sh" ]; then
#  . "$GORILLA_HOME/bin/setenv.sh"
#fi

# Ensure that neither GORILLA_HOME nor GORILLA_BASE contains a colon
# as this is used as the separator in the classpath and Java provides no
# mechanism for escaping if the same character appears in the path.
case $GORILLA_HOME in
*:*)
  echo "Using GORILLA_HOME:   $GORILLA_HOME"
  echo "Unable to start as GORILLA_HOME contains a colon (:) character"
  exit 1
  ;;
esac

# Add on extra jar files to CLASSPATH
if [ ! -z "$CLASSPATH" ]; then
  CLASSPATH="$CLASSPATH":
fi

# Bugzilla 37848: When no TTY is available, don't output to console
have_tty=0
if [ -t 0 ]; then
  have_tty=1
fi

if [ -z "$JSSE_OPTS" ]; then
  JSSE_OPTS="-Djdk.tls.ephemeralDHKeySize=2048"
fi
JAVA_OPTS="$JAVA_OPTS $JSSE_OPTS"

# Set UMASK unless it has been overridden
#if [ -z "$UMASK" ]; then
#    UMASK="0027"
#fi
#umask $UMASK

# Java 9 no longer supports the java.endorsed.dirs
# system property. Only try to use it if
# JAVA_ENDORSED_DIRS was explicitly set
# or GORILLA_HOME/endorsed exists.
#ENDORSED_PROP=ignore.endorsed.dirs
#if [ -n "$JAVA_ENDORSED_DIRS" ]; then
#    ENDORSED_PROP=java.endorsed.dirs
#fi
#if [ -d "$GORILLA_HOME/endorsed" ]; then
#    ENDORSED_PROP=java.endorsed.dirs
#fi

# Add the JAVA 9 specific start-up parameters required
#JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.base/java.lang=ALL-UNNAMED"
#JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.base/java.io=ALL-UNNAMED"
#JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED"
#export JDK_JAVA_OPTIONS

# ----- Execute The Requested Command -----------------------------------------

# Bugzilla 37848: only output this if we have a TTY
if [ $have_tty -eq 1 ]; then
  echo "Using GORILLA_HOME:   $GORILLA_HOME"
  if [ "$1" = "debug" ]; then
    echo "Using JAVA_HOME:       $JAVA_HOME"
  else
    echo "Using JRE_HOME:        $JRE_HOME"
  fi
  echo "Using CLASSPATH:       $CLASSPATH"
fi

#  if [ -z "$JPDA_TRANSPORT" ]; then
#    JPDA_TRANSPORT="dt_socket"
#  fi
#  if [ -z "$JPDA_ADDRESS" ]; then
#    JPDA_ADDRESS="localhost:8000"
#  fi
#  if [ -z "$JPDA_SUSPEND" ]; then
#    JPDA_SUSPEND="n"
#  fi
#  if [ -z "$JPDA_OPTS" ]; then
#    JPDA_OPTS="-agentlib:jdwp=transport=$JPDA_TRANSPORT,address=$JPDA_ADDRESS,server=y,suspend=$JPDA_SUSPEND"
#  fi
#  GORILLA_OPTS="$JPDA_OPTS $GORILLA_OPTS"

#eval exec "\"$_RUNJAVA\"" "$JAVA_OPTS" "$GORILLA_OPTS" \
eval exec "\"$_RUNJAVA\"" "$JAVA_OPTS" \
  -classpath "\"$CLASSPATH\"" \
  -jar "${GORILLA_HOME}/gorilla-notebook-standalone.jar" "$@"
#      -D$ENDORSED_PROP="\"$JAVA_ENDORSED_DIRS\"" \
#      -Djava.io.tmpdir="\"$CATALINA_TMPDIR\"" \
