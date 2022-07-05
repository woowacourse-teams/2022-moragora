import Discussion from '.';

export default {
  title: 'Components/Discussion',
  component: Discussion,
};

const Template = (args) => {
  return <Discussion {...args} />;
};

export const Default = Template.bind({});
