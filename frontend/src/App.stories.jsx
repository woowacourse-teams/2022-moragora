import React from 'react';
import App from './App';

export default {
  title: 'App/App',
  component: App,
};

const Template = (args) => {
  return <App></App>;
};

export const Default = Template.bind({});

Default.args = {};
