# TODO

## Misc

core.cljs:3718 re-frame: no  :event  handler registered for:  :palette-blurcljs.core.apply.cljs$core$IFn$_invoke$arity$2 @ core.cljs:3718cljs$core$apply @ core.cljs:3709re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic @ loggers.cljc:38re_frame$loggers$console @ loggers.cljc:35re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3 @ registrar.cljc:31re_frame$registrar$get_handler @ registrar.cljc:18re_frame$events$handle @ events.cljc:55re_frame.router.EventQueue.re_frame$router$IEventQueue$_process_1st_event_in_queue$arity$1 @ router.cljc:169re_frame$router$_process_1st_event_in_queue @ router.cljc:82re_frame.router.EventQueue.re_frame$router$IEventQueue$_run_queue$arity$1 @ router.cljc:188re_frame$router$_run_queue @ router.cljc:84(anonymous function) @ router.cljc:140re_frame.router.EventQueue.re_frame$router$IEventQueue$_fsm_trigger$arity$3 @ router.cljc:159re_frame$router$_fsm_trigger @ router.cljc:78(anonymous function) @ router.cljc:177channel.port1.onmessage @ nexttick.js:211
core.cljs:3718 Handling re-frame event:  [:app:commands]

- Service worker (js)
- lein assets compress
- Fix lein ring uberwar
- Deploy on heroku
- Add PWA stuff
- Enable standalone jar "drop in other webapp extension deployment" 
- Everything marked with TODO in the code
- Introduce next gen clj native worksheet persistence, save uuid
 to allow references
- Implement render/db tests, re-frame 

- Revisit (Clo)Jupyter
- test, devcards TDD
- Implement specs
- Implement viewer
- Explore local (brower repl/kernel)
- Drop in uberjar (with min aot)
- prod build
- Enable clipboard
- Enable config / parinfer (optionally)
- Build docker image allowing drop in jars (via volume)
- Introduce com.stuartsierra.component, maybe tesla ms
- Enable travis
- Try out dirac devtools
- Use timbre logging
- Fix Websocket failure handling/reconnect
- Authentication/Authorization
- POC Sparking/big data
- Kill long running threads on server