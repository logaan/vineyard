[#vineyard.core.Call
 {:fn-name   "log"
  :arguments [#vineyard.core.Text
              {:text "hi"}]}
 #vineyard.core.Call
 {:fn-name   "sleep"
  :arguments [#vineyard.core.AnonymousFunction
              {:parameters []
               :body [#vineyard.core.Call
                      {:fn-name   "log"
                       :arguments [#vineyard.core.Text
                                   {:text "bye"}]}]}]}]
