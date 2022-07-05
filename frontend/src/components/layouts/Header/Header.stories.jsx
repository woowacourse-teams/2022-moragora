import Header from '.';

export default {
  title: 'Components/Header',
  component: Header,
};

const Template = (args) => {
  return <Header {...args} />;
};

export const Default = Template.bind({});
