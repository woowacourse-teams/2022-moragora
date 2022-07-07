import Spinner from '.';

export default {
  title: 'Components/Spinner',
  component: Spinner,
};

function Template(args) {
  return <Spinner {...args} />;
}

export const Default = Template.bind({});
