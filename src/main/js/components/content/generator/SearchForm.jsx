import React from 'react';
import { FormSection } from 'redux-form';
import { ClipLoader } from 'react-spinners';
import FormModule from '../../FormModule';
import SearchList from '../../../containers/content/generator/SearchList';
import formDefaults from '../../../constants/formDefaults';

class SearchForm extends React.Component {
  componentWillMount() {
    const { firstRender, initialize, formValues } = this.props;
    if (firstRender) {
      initialize(formValues);
    }
  }

  componentDidMount() {
    $('.selectpicker').selectpicker('refresh');
  }

  componentDidUpdate() {
    $('.selectpicker').selectpicker('refresh');
  }

  render() {
    const {
      schedule,
      handleSubmit,
      submit,
      resetForm,
      removeCourse,
      isFetching,
    } = this.props;
    return (
      <form
        onSubmit={handleSubmit(submit)}
        onKeyPress={(e) => {
          if (e.key === 'Enter') {
            e.preventDefault();
          }
        }}
      >
        <div className="page-header no-margin-bottom">
          <h1 className="o">
            Restrictions
          </h1>
          <div className="alert alert-warning no-margin-bottom pad" role="alert">
            Generator links/URLs are being reworked and should be implemented soon.
            There isn&#39;t a way right now to return a previous search,
            sorry for the inconvenience!
          </div>
        </div>
        <div className="pad-bottom">
          <div className="pad-top">
            <h4>
              Term
            </h4>
            <FormModule
              type="select"
              name="term"
              width="auto"
              values={formDefaults.terms}
              onChange={resetForm}
            />
          </div>
          <div className="flex-container">
            <div className="pad-top margin-right">
              <h4>
                Start Time
              </h4>
              <div className="flex-container">
                <div className="no-pad">
                  <FormModule
                    type="select"
                    name="h1"
                    width="auto"
                    values={['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']}
                  />
                </div>
                <div className="no-pad">
                  <FormModule
                    type="select"
                    name="m1"
                    width="auto"
                    values={['00', '05', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55']}
                  />
                </div>
                <div className="no-pad">
                  <FormModule
                    type="select"
                    name="start"
                    width="auto"
                    values={['AM', 'PM']}
                  />
                </div>
              </div>
            </div>
            <div className="pad-top margin-right">
              <h4>
                End Time
              </h4>
              <div className="flex-container">
                <div className="no-pad">
                  <FormModule
                    type="select"
                    name="h2"
                    width="auto"
                    values={['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']}
                  />
                </div>
                <div className="no-pad">
                  <FormModule
                    type="select"
                    name="m2"
                    width="auto"
                    values={['00', '05', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55']}
                  />
                </div>
                <div className="no-pad">
                  <FormModule
                    type="select"
                    name="end"
                    width="auto"
                    values={['AM', 'PM']}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="flex-container">
            <div className="pad-top margin-right">
              <h4>
                Gap Time
              </h4>
              <FormModule
                type="select"
                name="gap"
                width="auto"
                values={['0', '5', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55', '60']}
              />
            </div>
            <div className="pad-top margin-right">
              <h4>
                Free Days
              </h4>
              <FormModule
                type="select"
                name="free"
                // className="form-control"
                values={[{
                  name: 'Monday',
                  value: 'M',
                }, {
                  name: 'Tuesday',
                  value: 'T',
                }, {
                  name: 'Wednesday',
                  value: 'W',
                }, {
                  name: 'Thursday',
                  value: 'R',
                }, {
                  name: 'Friday',
                  value: 'F',
                }]}
                multiple
              />
            </div>
          </div>
        </div>
        <div className="no-pad">
          <div className="flex-container">
            <h3 className="pad-right">
              {`Current Schedule${schedule.length === 12 ? ' (Max)' : ''}`}
            </h3>
            { isFetching && <ClipLoader size={25} color="darkorange" /> }
          </div>
          <div className="pad-top">
            <SearchList />
          </div>
          <FormSection name="courses">
            <div className="table-responsive">
              <table id="schedule" className="table">
                <thead className="thead-inverse">
                  <tr>
                    <th>
                      CRN
                    </th>
                    <th>
                      Course
                    </th>
                    <th>
                      Title
                    </th>
                    <th>
                      Class Type
                    </th>
                    <th>
                      Professor
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  { schedule.map(courseList => (
                    <FormSection
                      key={`${courseList[0].subject}${courseList[0].courseNumber}${courseList.id}`}
                      name={`${courseList[0].subject}${courseList[0].courseNumber}${courseList.id}`}
                    >
                      <tr>
                        <td>
                          <FormModule
                            type="select"
                            name="crns"
                            values={courseList.map(course => `${course.crn}`)}
                            multiple
                            title="Any"
                          />
                        </td>
                        <td>
                          {`${courseList[0].subject} ${courseList[0].courseNumber}`}
                        </td>
                        <td>
                          {courseList[0].name}
                        </td>
                        <td>
                          <FormModule
                            type="select"
                            name="types"
                            values={[...new Set(courseList.map(course => course.type))]}
                            multiple
                            title="Any"
                          />
                        </td>
                        <td>
                          <FormModule
                            type="select"
                            name="instructors"
                            values={[...new Set(courseList.map(course => course.instructor))]}
                            multiple
                            title="Any"
                          />
                        </td>
                        <td>
                          <button
                            className="btn btn-default"
                            type="button"
                            onClick={() => removeCourse(courseList)}
                          >
                            Remove
                          </button>
                        </td>
                      </tr>
                    </FormSection>
                  ))}
                </tbody>
              </table>
            </div>
          </FormSection>
        </div>
        <div className="pad-top border-top">
          <button className="btn btn-default btn-lg" type="submit">
            Create Schedules
          </button>
        </div>
      </form>
    );
  }
}

export default SearchForm;
