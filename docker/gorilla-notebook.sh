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
#   JPDA_TRANSPORT  (Optional) JPDA transport used when the "jpda start"
#                   command is executed. The default is "dt_socket".
#
#   JPDA_ADDRESS    (Optional) Java runtime options used when the "jpda start"
#                   command is executed. The default is localhost:8000.
#
#   JPDA_SUSPEND    (Optional) Java runtime options used when the "jpda start"
#                   command is executed. Specifies whether JVM should suspend
#                   execution immediately after startup. Default is "n".
#
#   JPDA_OPTS       (Optional) Java runtime options used when the "jpda start"
#                   command is executed. If used, JPDA_TRANSPORT, JPDA_ADDRESS,
#                   and JPDA_SUSPEND are ignored. Thus, all required jpda
#                   options MUST be specified. The default is:
#
# -----------------------------------------------------------------------------

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set GORILLA_HOME if not already set
[ -z "$GORILLA_HOME" ] && GORILLA_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

# Copy GORILLA_BASE from GORILLA_HOME if not already set
[ -z "$GORILLA_BASE" ] && GORILLA_BASE="$GORILLA_HOME"

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
  *:*) echo "Using GORILLA_HOME:   $GORILLA_HOME";
       echo "Unable to start as GORILLA_HOME contains a colon (:) character";
       exit 1;
esac

# Add on extra jar files to CLASSPATH
if [ ! -z "$CLASSPATH" ] ; then
  CLASSPATH="$CLASSPATH":
fi

# Bugzilla 37848: When no TTY is available, don't output to console
have_tty=0
if [ -t 0 ]; then
    have_tty=1
fi

if [ -z "$JSSE_OPTS" ] ; then
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

# Add the JAVA 9 specific start-up parameters required by Tomcat
#JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.base/java.lang=ALL-UNNAMED"
#JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.base/java.io=ALL-UNNAMED"
#JDK_JAVA_OPTIONS="$JDK_JAVA_OPTIONS --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED"
#export JDK_JAVA_OPTIONS

# ----- Execute The Requested Command -----------------------------------------

# Bugzilla 37848: only output this if we have a TTY
if [ $have_tty -eq 1 ]; then
  echo "Using GORILLA_HOME:   $GORILLA_HOME"
  if [ "$1" = "debug" ] ; then
    echo "Using JAVA_HOME:       $JAVA_HOME"
  else
    echo "Using JRE_HOME:        $JRE_HOME"
  fi
  echo "Using CLASSPATH:       $CLASSPATH"
fi

if [ "$1" = "jpda" ] ; then
  if [ -z "$JPDA_TRANSPORT" ]; then
    JPDA_TRANSPORT="dt_socket"
  fi
  if [ -z "$JPDA_ADDRESS" ]; then
    JPDA_ADDRESS="localhost:8000"
  fi
  if [ -z "$JPDA_SUSPEND" ]; then
    JPDA_SUSPEND="n"
  fi
  if [ -z "$JPDA_OPTS" ]; then
    JPDA_OPTS="-agentlib:jdwp=transport=$JPDA_TRANSPORT,address=$JPDA_ADDRESS,server=y,suspend=$JPDA_SUSPEND"
  fi
  GORILLA_OPTS="$JPDA_OPTS $GORILLA_OPTS"
  shift
fi

if [ "$1" = "run" ]; then

  shift
  if [ "$1" = "-security" ] ; then
    if [ $have_tty -eq 1 ]; then
      echo "Using Security Manager"
    fi
    shift
    eval exec "\"$_RUNJAVA\"" "$JAVA_OPTS" "$GORILLA_OPTS" \
      -D$ENDORSED_PROP="\"$JAVA_ENDORSED_DIRS\"" \
      -classpath "\"$CLASSPATH\"" \
#      -Djava.security.manager \
#      -Djava.security.policy=="\"$GORILLA_BASE/conf/catalina.policy\"" \
#      -Djava.io.tmpdir="\"$CATALINA_TMPDIR\"" \
      -jar gorilla-notebook-standalone.jar "$@"
  else
    eval exec "\"$_RUNJAVA\"" "$JAVA_OPTS" "$GORILLA_OPTS" \
      -D$ENDORSED_PROP="\"$JAVA_ENDORSED_DIRS\"" \
      -classpath "\"$CLASSPATH\"" \
#      -Djava.io.tmpdir="\"$CATALINA_TMPDIR\"" \
      -jar gorilla-notebook-standalone.jar "$@"
  fi

else

  echo "Usage: gorilla-notebook.sh ( commands ... )"
  echo "commands:"
  echo "  jpda start        Start Gorilla Notebooke under JPDA debugger"
  echo "  run               Start Gorilla Notebook in the current window"
  echo "  run -security     Start in the current window with security manager"
  echo "  version           What version of Gorilla Notebook are you running?"
  exit 1

fi
