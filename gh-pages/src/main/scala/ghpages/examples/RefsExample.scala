package ghpages.examples

import ghpages.GhPagesMacros
import japgolly.scalajs.react._, vdom.prefix_<^._
import org.scalajs.dom.raw.HTMLInputElement
import ghpages.examples.util.SideBySide

object RefsExample {

  def content = SideBySide.Content(jsSource, source, main())

  lazy val main = addIntro(App, _(scalaPortOfPage("docs/more-about-refs.html")))

  val jsSource =
    """
      |var App = React.createClass({
      |  getInitialState: function() {
      |    return {userInput: ''};
      |  },
      |
      |  handleChange: function(e) {
      |    this.setState({userInput: e.target.value});
      |  },
      |
      |  clearAndFocusInput: function() {
      |    // Clear the input
      |    this.setState({userInput: ''}, function() {
      |      // This code executes after the component is re-rendered
      |      this.refs.theInput.focus();   // Boom! Focused!
      |    });
      |  },
      |
      |  render: function() {
      |    return (
      |      <div>
      |        <div onClick={this.clearAndFocusInput}>
      |          Click to Focus and Reset
      |        </div>
      |        <input
      |          ref      = "theInput"
      |          value    = {this.state.userInput}
      |          onChange = {this.handleChange}
      |        />
      |      </div>
      |    );
      |  }
      |});
      |""".stripMargin


  val source = GhPagesMacros.exampleSource

  // EXAMPLE:START

  val theInput = Ref[HTMLInputElement]("theInput")

  class Backend($: BackendScope[Unit, String]) {
    def handleChange(e: ReactEventI) =
      $.setState(e.target.value)

    def clearAndFocusInput() =
      $.setState("", theInput($).tryFocus)

    def render(state: String) =
      <.div(
        <.div(
          ^.onClick --> clearAndFocusInput,
          "Click to Focus and Reset"),
        <.input(
          ^.ref       := theInput,
          ^.value     := state,
          ^.onChange ==> handleChange))
  }

  val App = ReactComponentB[Unit]("App")
    .initialState("")
    .renderBackend[Backend]
    .build

  // EXAMPLE:END
}
