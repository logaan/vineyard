[#vineyard.data.Call
 {:fn-name   "log"
  :arguments [#vineyard.data.Text
              {:text "hi"}]}
 #vineyard.data.Call
 {:fn-name   "sleep"
  :arguments [#vineyard.data.AnonymousFunction
              {:parameters []
               :body [#vineyard.data.Call
                      {:fn-name   "log"
                       :arguments [#vineyard.data.Text
                                   {:text "ok"}]}
                      #vineyard.data.Call
                      {:fn-name   "sleep"
                       :arguments [#vineyard.data.AnonymousFunction
                                   {:parameters []
                                    :body [#vineyard.data.Call
                                           {:fn-name   "log"
                                            :arguments [#vineyard.data.Text
                                                        {:text "bye"}]}]}]}]}]}]
