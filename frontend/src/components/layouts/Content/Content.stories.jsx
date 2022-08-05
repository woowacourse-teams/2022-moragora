import Content from '.';

export default {
  title: 'Components/Content',
  component: Content,
};

const Template = (args) => {
  return <Content {...args} />;
};

export const Default = Template.bind({});
