import React from 'react';
import { Field } from 'redux-form';

const FormModule = ({
  type,
  name,
  onBlur,
  onChange,
  onFocus,
  autocomplete,
  placeholder,
  className = '',
  width,
  multiple,
  values,
  title,
}) => {
  let formModule;
  switch (type) {
    case 'input':
      formModule = (
        <Field
          name={name}
          className="form-control"
          component="input"
          onBlur={onBlur}
          onChange={onChange}
          onFocus={onFocus}
          type="text"
          autoComplete={(autocomplete ? 'on' : 'off')}
          placeholder={placeholder}
        />
      );
      break;
    case 'select':
      formModule = (
        <Field
          name={name}
          className={`selectpicker ${className}`}
          component="select"
          data-width={width}
          multiple={multiple}
          type={multiple ? 'select-multiple' : ''}
          title={title}
          onChange={onChange}
          data-container="body"
        >
          { values && values.map((val) => {
            const optionName = val.name || val;
            const valLower = (val.value || val).toLowerCase();
            return (
              <option key={valLower} value={valLower}>
                {optionName}
              </option>
            );
          })}
        </Field>
      );
      break;
    default:
      formModule = <div />;
  }
  return formModule;
};

export default FormModule;
