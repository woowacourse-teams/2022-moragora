import App from './App';

export default {
  title: 'App/App',
  component: App,
};

const Template = (args) => {
  return <App {...args} />;
};

export const Default = Template.bind({});
