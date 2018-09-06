import React from 'react';
import { ClipLoader } from 'react-spinners';
import ScheduleVisualTable from '../ScheduleVisualTable';
import CourseTable from '../../containers/content/builder/CourseTable';
import SearchList from '../../containers/content/builder/SearchList';
import FormModule from '../FormModule';
import formDefaults from '../../constants/formDefaults';

class Schedules extends React.Component {
  constructor(props) {
    super(props);
    this.toggleTextTable = this.toggleTextTable.bind(this);
    this.toggleVisualTable = this.toggleVisualTable.bind(this);
    const { initialize } = props;
    initialize({
      term: formDefaults.termValue,
    });
    this.state = {
      showTextTable: true,
      showVisualTable: true,
    };
  }

  componentDidMount() {
    $('.selectpicker').selectpicker('refresh');
  }

  toggleTextTable() {
    this.setState(prevState => ({ showTextTable: !prevState.showTextTable }));
  }

  toggleVisualTable() {
    this.setState(prevState => ({ showVisualTable: !prevState.showVisualTable }));
  }

  render() {
    const {
      schedule,
      isFetching,
      resetBuilder,
    } = this.props;
    const {
      showTextTable,
      showVisualTable,
    } = this.state;
    return (
      <div>
        <div id="schedules-bar" className="flex-container-spaced no-print">
          <div className="flex-container">
            <div className="pad-right">
              <FormModule
                type="select"
                name="term"
                width="auto"
                onChange={() => resetBuilder()}
                values={formDefaults.terms}
              />
            </div>
            { isFetching && <ClipLoader size={25} color="darkorange" /> }
          </div>
          <h4>
            Schedule Builder
          </h4>
          <div>
            <button type="button" className="btn btn-default" onClick={this.toggleTextTable}>
              Toggle Text
            </button>
            <button type="button" className="btn btn-default" onClick={this.toggleVisualTable}>
              Toggle Visual
            </button>
            <button
              onClick={window.print}
              type="button"
              className="btn btn-default"
            >
              <span className="glyphicon glyphicon-print" />
            </button>
          </div>
        </div>
        <div className="pad-top">
          <SearchList />
        </div>
        { showTextTable && (
          <CourseTable />
        )}
        { showVisualTable && <ScheduleVisualTable schedule={schedule} /> }
      </div>
    );
  }
}

export default Schedules;
